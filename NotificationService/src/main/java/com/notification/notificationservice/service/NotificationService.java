package com.notification.notificationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.notificationservice.DTO.Notification;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    private final JavaMailSender mailSender;

    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;

    @Autowired
    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "notifications")
    public void sendNotification(String rawNotification) {
        Notification notification = null;
        try {
            notification = mapper.readValue(rawNotification, Notification.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        sendEmail(notification);
        sendSms(notification);
    }

    @PostConstruct
    private void initTwilio() {
        Twilio.init(accountSid, authToken);
    }

    @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    private void sendEmail(Notification notification) {
        log.info("попытка отправки Email на: {}", notification.getEmail());
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(notification.getEmail());
        simpleMailMessage.setSubject(notification.getTitle());
        simpleMailMessage.setText(notification.getDescription());

        mailSender.send(simpleMailMessage);
        log.info("Email успешно отправлен на: {}", notification.getEmail());
    }

    @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public void sendSms(Notification notification) {
        log.info("Попытка отправки SMS на: {}", notification.getPhoneNumber());

        Message message = Message.creator(
                new PhoneNumber(notification.getPhoneNumber()),
                new PhoneNumber(twilioPhoneNumber),
                notification.getTitle() + "\n" + notification.getDescription()
        ).create();

        log.info("SMS отправлен (SID: {})", message.getSid());
        }

    }
