package com.crm.corecrm.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "assignedTo")
@EqualsAndHashCode
public class Tasks {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "building_seq_gen")
    @SequenceGenerator(name = "building_seq_gen", sequenceName = "building_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to", nullable = false)
    private Users assignedTo;

    @NotNull
    @FutureOrPresent(message = "Введите корректную дату и время срока срока выполнения")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "building_id", referencedColumnName = "id")
    private Building building;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum Status {
        NEW,
        IN_PROGRESS,
        COMPLETED
    }
    public enum Priority {
        NONE,
        LOW,
        MEDIUM,
        HIGH
    }
}
