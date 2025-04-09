package com.example.springpet.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TaskDTO {
    @NonNull
    String name;

    String description;

    @NonNull
    LocalDate deadline;

    @NonNull
    Status status;

    public TaskDTO(Task task) {
        this.name = task.getName();
        this.description = task.getDescription();
        this.deadline = task.getDeadline();
        this.status = task.getStatus();
    }
}
