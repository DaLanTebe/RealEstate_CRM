package com.adapter.contentprocessor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

@Entity
public class Estate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Pattern(
            regexp = "^(\\d{2}:\\d{2}:\\d{6,7}:\\d{1,4})$",
            message = "Неверный формат кадастрового номера. Ожидаемый формат: XX:XX:XXXXXXX:XXX"
    )
    private String cadastr;

    @NotNull
    private Type type;


    @NotNull(message = "Укажите площадь")
    private String square;

    @ElementCollection
    @CollectionTable(name = "estate_price", joinColumns = @JoinColumn(name = "estate_id"))
    @MapKeyColumn(name = "price_key")
    @Column(name = "price_value")
    private HashMap<String, BigDecimal> price;


    public enum Type {
        COMMERCIAL,
        RESIDENTIAL,
        GARAGE;
    }
}
