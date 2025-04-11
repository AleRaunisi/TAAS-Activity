
echo "Stopping and removing containers..."
docker-compose down

mvn clean install

echo "Building gateway project..."
cd gateway && mvn clean install
cd ..

echo "Rebuilding and starting containers..."
docker-compose -f docker-compose.yml up --build