package com.crm.corecrm.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class Notification {
    @NotBlank
    private String email;
    @NotNull(message = "error")
    private String phoneNumber;
    private String title;
    private String description;
}
