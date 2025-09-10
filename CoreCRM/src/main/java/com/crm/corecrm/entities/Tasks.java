package com.crm.corecrm.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

//@Entity
public class Tasks {

//    @Id
//    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private Long assignedTo;
    private Long relatedBuildingId;
    private LocalDate dueDate;
    private String status;
    private String priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
