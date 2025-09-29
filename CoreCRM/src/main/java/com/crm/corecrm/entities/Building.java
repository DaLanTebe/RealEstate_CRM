package com.crm.corecrm.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "building_seq_gen")
    @SequenceGenerator(name = "building_seq_gen", sequenceName = "building_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "Адрес обязателен")
    private String address;

    @NotBlank(message = "Кадастровый номер обязателен")
    @Pattern(
            regexp = "^(\\d{2}:\\d{2}:\\d{6,7}:\\d{1,4})$",
            message = "Неверный формат кадастрового номера. Ожидаемый формат: XX:XX:XXXXXXX:XXX"
    )
    @Column(unique = true)
    private String cadastralNumber;

    @NotNull(message = "Укажите площадь")
    @Min(value = 0, message = "Площадь должна быть положительной")
    private Double square;

    @Pattern(regexp = "\\+\\d{1,15}", message = "номер телефона должен быть в международном формате, например +123456789")
    @NotNull(message = "телефон владельца обязателен")
    private String telNumber;

    @NotNull(message = "Укажите цену")
    @Positive(message = "Цена должна быть положительной")
    private BigDecimal price;

    @NotBlank(message = "Описание обязательно")
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        NOTASSIGNED,
        ASSIGNED,
        SOLD
    }
}
