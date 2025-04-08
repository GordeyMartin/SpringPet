package com.example.springpet.controller;

import com.example.springpet.model.Status;
import com.example.springpet.model.Task;
import com.example.springpet.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.findAllTasks());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.saveTask(task));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> editTask(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(taskService.editTask(id, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtered-tasks")
    public ResponseEntity<List<Task>> getFilteredTasks(@RequestParam Status status) {
        return ResponseEntity.ok(taskService.filterAllTasks(status));
    }

    @GetMapping("/sorted-tasks")
    public ResponseEntity<List<Task>> getSortedTask() {
        return ResponseEntity.ok(taskService.sortAllTasks());
    }
}
