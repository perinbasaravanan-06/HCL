package com.hcl.backend.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("food.exchange");
    }

    @Bean
    Queue orderNotificationQueue() {
        return new Queue("order.notification.queue", false);
    }

    @Bean
    Queue inventoryAlertQueue() {
        return new Queue("inventory.alert.queue", false);
    }

    @Bean
    Binding orderPlacedBinding(Queue orderNotificationQueue, TopicExchange exchange) {
        return BindingBuilder.bind(orderNotificationQueue).to(exchange).with("order.placed");
    }

    @Bean
    Binding orderStatusUpdatedBinding(Queue orderNotificationQueue, TopicExchange exchange) {
        return BindingBuilder.bind(orderNotificationQueue).to(exchange).with("order.status.updated");
    }

    @Bean
    Binding inventoryLowStockBinding(Queue inventoryAlertQueue, TopicExchange exchange) {
        return BindingBuilder.bind(inventoryAlertQueue).to(exchange).with("inventory.low.stock");
    }
}
