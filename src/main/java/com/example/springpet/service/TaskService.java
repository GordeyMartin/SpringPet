package com.example.springpet.service;

import com.example.springpet.model.Field;
import com.example.springpet.model.Status;
import com.example.springpet.model.Task;
import com.example.springpet.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> filterAllTasks(Status status) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getStatus() == status)
                .toList();
    }

    public List<Task> sortAllTasks() {
        return taskRepository.findAll().stream()
                .sorted((o1, o2) -> o1.getDeadline().compareTo(o2.getDeadline()))
                .toList();
    }

    public Task findTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Task editTask(Long id, Map<String, Object> updates) {
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            Field field = Field.valueOf(entry.getKey().toUpperCase().replace(" ", "_"));
            switch (field) {
                case NAME -> taskRepository.updateName(id, entry.getValue().toString());
                case DESCRIPTION -> taskRepository.updateDescription(id, entry.getValue().toString());
                case DEADLINE -> taskRepository.updateDeadline(id, LocalDate.parse(entry.getValue().toString()));
                case STATUS -> taskRepository.updateStatus(id, Status.valueOf(entry.getValue().toString().toUpperCase().replace(" ", "_")));
            }
        }
        return findTaskById(id);
    }
}
