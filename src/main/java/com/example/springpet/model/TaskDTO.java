package com.example.springpet.model;

import jakarta.validation.constraints.NotNull;
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

    private Long id;

    @NotNull(message = "Name should not be empty")
    String name;

    String description;

    @NotNull(message = "Deadline should not be empty")
    LocalDate deadline;

    @NotNull(message = "Status should not be empty")
    Status status;

    public TaskDTO(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.deadline = task.getDeadline();
        this.status = task.getStatus();
    }
}
