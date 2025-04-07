package com.TASS.gateway.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    // Configurazione RabbitMQ
    @Bean
    public Queue taskQueue() {
        return new Queue("telegramNotifications", true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("tasks-exchange");
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("task.created");
    }

    // Configurazione delle route
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("main_app_route_api", r -> r
                .path("/api/**")
                .filters(f -> f.rewritePath("/api/(?<segment>.*)", "/${segment}"))
                .uri("http://host.docker.internal:8080")
            )
            .route("main_app_route_auth_static", r -> r
                .path("/auth/**", "/static/**")
                .uri("http://host.docker.internal:8080")
            )
            .route("admin_route", r -> r
                .path("/admin/**")
                .uri("http://host.docker.internal:8080/admin")
            )
            .build();
    }
}