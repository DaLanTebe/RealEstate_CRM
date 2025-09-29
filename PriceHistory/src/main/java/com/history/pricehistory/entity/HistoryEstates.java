package com.history.pricehistory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class HistoryEstates {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Pattern(
            regexp = "^(\\d{2}:\\d{2}:\\d{6,7}:\\d{1,4})$",
            message = "Неверный формат кадастрового номера. Ожидаемый формат: XX:XX:XXXXXXX:XXX"
    )
    @Column(unique = true, nullable = false)
    @NotNull
    private String cadastr;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Type type;


    @NotNull(message = "Укажите площадь")
    private String square;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PriceType priceType;

    @NotNull
    private BigDecimal price;

    @CreationTimestamp
    private LocalDateTime creationDate;


    public enum Type {
        COMMERCIAL,
        RESIDENTIAL,
        GARAGE;
    }
    public enum PriceType {
        CIAN,
        DOMCLICK,
        ROSREESTR
    }
}