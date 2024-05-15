package com.goldengit.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonMessage = mapper.writeValueAsString(emailMessage);
            amqpTemplate.convertAndSend(exchangeName, routingKey, jsonMessage);
        } catch (JsonProcessingException jsonProcessingException) {
            log.error(jsonProcessingException.getMessage(), jsonProcessingException);
        }
    }
}
