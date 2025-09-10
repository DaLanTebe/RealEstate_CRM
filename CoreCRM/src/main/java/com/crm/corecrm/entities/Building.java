package com.crm.corecrm.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//@Entity
public class Building {

//    @Id
//    @GeneratedValue
    private Long id;
    private String address;
    private String cadastralNumber;
    private String type;
    private Double square;
    private BigDecimal price;
    private String description;
    private Long ownerId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
