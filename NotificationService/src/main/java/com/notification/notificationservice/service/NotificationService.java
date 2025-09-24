package com.notification.notificationservice.service;

import com.notification.notificationservice.DTO.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    private final JavaMailSender mailSender;

    @Autowired
    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "notification")
    public void sendNotification(Notification notification) {
        sendEmail(notification);
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
    public void sendSmsWithRetry(Notification notification) {
        log.info("Попытка отправки SMS на: {}", notification.getPhoneNumber());

    }

    @Recover
    public void recoverSms(Notification notification, Exception ex) {
        log.error("Восстановление: все retry провалились для {}: {}: {}",
                notification.getPhoneNumber(), notification.getEmail(), ex.getMessage());
    }
}
