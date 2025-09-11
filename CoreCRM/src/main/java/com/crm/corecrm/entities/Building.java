package com.crm.corecrm.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

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
    private String cadastralNumber;

    @NotNull(message = "Укажите площадь")
    @Min(value = 0, message = "Площадь должна быть положительной")
    private Double square;

    @NotNull(message = "Укажите цену")
    @Positive(message = "Цена должна быть положительной")
    private BigDecimal price;

    @NotBlank(message = "Описание обязательно")
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        ACTIVE,
        SOLD,
    }
}
