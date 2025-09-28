package com.adapter.contentprocessor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Estate {

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "estate_price", joinColumns = @JoinColumn(name = "estate_id"))
    @MapKeyColumn(name = "price_key")
    @Column(name = "price_value")
    private Map<String, BigDecimal> price = new HashMap<>();


    public enum Type {
        COMMERCIAL,
        RESIDENTIAL,
        GARAGE;
    }
}
