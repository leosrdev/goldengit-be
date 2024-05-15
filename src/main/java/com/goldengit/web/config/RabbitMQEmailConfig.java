package com.goldengit.web.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQEmailConfig {

    @Value("${rabbitmq.email.queue}")
    private String queueName;

    @Value("${rabbitmq.email.exchange}")
    private String exchangeName;

    @Value("${rabbitmq.email.routing.key}")
    private String routingKey;

    @Bean
    public Queue queue() {
        return new Queue(queueName);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue())
                .to(topicExchange())
                .with(routingKey);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter("com.goldengit.web.dto");
    }
}
