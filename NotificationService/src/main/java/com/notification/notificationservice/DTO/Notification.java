package com.notification.notificationservice.DTO;

import lombok.Data;

@Data
public class Notification {
    private String email;
    private String phoneNumber;
    private String title;
    private String description;
}
