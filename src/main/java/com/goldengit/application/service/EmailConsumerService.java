package com.goldengit.application.service;

import com.goldengit.infra.config.EmailConfig;
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

    @Autowired
    private EmailConfig emailConfig;

    @Value("${goldengit.account.activate.url}")
    private String activateURL;

    @RabbitListener(queues = "${rabbitmq.email.queue}")
    public void processMessage(EmailMessage emailMessage) {
        String link = String.format("%s?t=%s", activateURL, emailMessage.getActivationToken());
        String formattedText = String.format(emailConfig.getEmailText(), emailMessage.getName(), link);
        emailSenderService.sendSimpleMessage(
                emailConfig.getEmailFrom(),
                emailMessage.getEmail(),
                emailConfig.getEmailSubject(),
                formattedText
        );
        log.info(String.format("Email sent to: %s", emailMessage.getEmail()));
    }
}
