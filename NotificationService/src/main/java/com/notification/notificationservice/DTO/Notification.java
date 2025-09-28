package com.notification.notificationservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private String email;
    private String phoneNumber;
    private String title;
    private String description;
}
