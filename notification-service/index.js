const amqp = require("amqplib");
const TelegramBot = require("node-telegram-bot-api");
require("dotenv").config();

// Configurazione Telegram
const TELEGRAM_TOKEN = process.env.TELEGRAM_TOKEN;
const CHAT_ID = process.env.CHAT_ID;
const bot = new TelegramBot(TELEGRAM_TOKEN, { polling: false });

// Configurazione RabbitMQ
const RABBITMQ_URL = "amqp://user:password@rabbitmq";
const EXCHANGE_NAME = "tasks-exchange";
const QUEUE_NAME = "telegramNotifications";
const ROUTING_KEY = "task.created";

async function startConsumer() {
    try {
        const connection = await amqp.connect(RABBITMQ_URL);
        const channel = await connection.createChannel();

        // Dichiara l'exchange di tipo "topic"
        await channel.assertExchange(EXCHANGE_NAME, "topic", { durable: true });

        // Dichiara la coda e il binding
        await channel.assertQueue(QUEUE_NAME, { durable: true });
        await channel.bindQueue(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

        console.log("✅ Consumer in ascolto sulla coda:", QUEUE_NAME);

        channel.consume(QUEUE_NAME, (msg) => {
            if (msg !== null) {
                const message = msg.content.toString();
                console.log("📨 Messaggio ricevuto:", message);

                // Invia a Telegram
                bot.sendMessage(CHAT_ID, message)
                    .then(() => {
                        console.log("✅ Messaggio inviato a Telegram");
                        channel.ack(msg);
                    })
                    .catch(err => {
                        console.error("❌ Errore Telegram:", err.message);
                        channel.nack(msg);
                    });
            }
        });
    } catch (err) {
        console.error("❌ Errore connessione RabbitMQ:", err.message);
        setTimeout(startConsumer, 5000); // Ritenta dopo 5 secondi
    }
}

startConsumer();