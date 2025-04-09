package com.example.springpet.controller;

import com.example.springpet.model.Status;
import com.example.springpet.model.TaskDTO;
import com.example.springpet.model.exceptions.InvalidArgumentException;
import com.example.springpet.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.findAllTasks());
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
            throw new InvalidArgumentException(errorMessage);
        }
        return ResponseEntity.ok(taskService.saveTask(taskDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskDTO> editTask(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(taskService.editTask(id, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtered-tasks")
    public ResponseEntity<List<TaskDTO>> getFilteredTasks(@RequestParam Status status) {
        return ResponseEntity.ok(taskService.filterAllTasks(status));
    }

    @GetMapping("/sorted-tasks")
    public ResponseEntity<List<TaskDTO>> getSortedTask() {
        return ResponseEntity.ok(taskService.sortAllTasks());
    }
}
