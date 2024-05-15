package com.goldengit.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailSenderService {
    @RabbitListener(queues = "${rabbitmq.email.queue}")
    public void processMessage(String message) {
        log.info(message);
    }
}
