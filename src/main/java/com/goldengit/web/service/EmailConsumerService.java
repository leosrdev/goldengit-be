package com.goldengit.web.service;

import com.goldengit.web.dto.EmailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailConsumerService {

    @Autowired
    private EmailSenderService emailSenderService;

    @Value("${goldengit.api.domain}")
    private String domain;
    @Value("${account.activation.email.subject}")
    private String subject;
    @Value("${account.activation.email.text}")
    private String text;


    @RabbitListener(queues = "${rabbitmq.email.queue}")
    public void processMessage(EmailMessage emailMessage) {
        String link = String.format("https://%s/activate?t=%s", domain, emailMessage.getActivationToken());
        String formattedText = String.format(text, emailMessage.getName(), link);
        emailSenderService.sendSimpleMessage(emailMessage.getEmail(), subject, formattedText);
        log.info(String.format("Email sent to: %s", emailMessage.getEmail()));
    }
}
