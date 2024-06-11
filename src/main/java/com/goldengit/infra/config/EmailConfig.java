package com.goldengit.infra.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:email-config.yaml")
@Data
public class EmailConfig {
    @Value("${email-from}")
    private String emailFrom;

    @Value("${email-subject}")
    private String emailSubject;

    @Value("${email-text}")
    private String emailText;
}
