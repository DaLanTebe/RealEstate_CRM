package com.crm.corecrm.entities;

import com.crm.corecrm.customValidation.user.UniqueEmail;
import com.crm.corecrm.customValidation.user.UniqueUsername;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"password", "username"})
@EqualsAndHashCode(exclude = {"password", "username", "updatedAt"})
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 3, max = 20, message = "Имя пользователя должно состоять из 3 - 20 символов")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "Имя пользователя должно начинаться с буквы и может содержать только буквы, цифры и подчеркивания")
    @UniqueUsername
    private String username;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 100, message = "Пароль должен содержать 8 - 100 символов")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?:[@#$%^&+=!]*)$",
            message = "Пароль должен содержать хотя бы 1 цифру, 1 строчную и 1 заглавную букву"
    )
    private String password;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ\\s\\-']+$",
            message = "Имя может содержать только буквы, пробелы, дефисы и апострофы")
    private String firstName;

    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(min = 2, max = 50, message = "Фамилия должна быть от 2 до 50 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ\\s\\-']+$",
            message = "Фамилия может содержать только буквы, пробелы, дефисы и апострофы")
    private String lastName;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Введите корректный email адрес")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Email должен быть в формате example@domain.com")
    @UniqueEmail()
    private String email;

    @NotBlank(message = "Номер телефона не может быть пустым")
    @Pattern(regexp = "^\\+7\\s?\\(?[0-9]{3}\\)?\\s?[0-9]{3}[\\-\\s]?[0-9]{2}[\\-\\s]?[0-9]{2}$",
            message = "Введите номер в формате +7 (XXX) XXX-XX-XX")
    private String phoneNumber;


    private UserRole role;
    private UserStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

//    @OneToMany
//    private List<Tasks> tasksList;

    public enum UserRole {
        ADMIN, MANAGER, USER, AGENT, CLIENT, UNKNOWN;
    }

    public enum UserStatus {
        ACTIVE, INACTIVE, PENDING, BLOCKED, DELETED
    }
}
