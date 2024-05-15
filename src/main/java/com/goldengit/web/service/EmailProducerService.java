package com.goldengit.web.service;

import com.goldengit.web.dto.EmailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailProducerService {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${rabbitmq.email.exchange}")
    private String exchangeName;

    @Value("${rabbitmq.email.routing.key}")
    private String routingKey;

    public void publishEmailMessage(EmailMessage emailMessage) {
        amqpTemplate.convertAndSend(exchangeName, routingKey, emailMessage);
    }
}
