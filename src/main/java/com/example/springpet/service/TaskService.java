package com.example.springpet.service;

import com.example.springpet.model.Field;
import com.example.springpet.model.Status;
import com.example.springpet.model.Task;
import com.example.springpet.model.TaskDTO;
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

    public List<TaskDTO> findAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(task -> new TaskDTO(task))
                .toList();
    }

    public TaskDTO saveTask(TaskDTO taskDto) {
        Task task = new Task(taskDto);
        return new TaskDTO(taskRepository.save(task));
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<TaskDTO> filterAllTasks(Status status) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getStatus() == status)
                .map(task -> new TaskDTO(task))
                .toList();
    }

    public List<TaskDTO> sortAllTasks() {
        return taskRepository.findAll().stream()
                .sorted((o1, o2) -> o1.getDeadline().compareTo(o2.getDeadline()))
                .map(task -> new TaskDTO(task))
                .toList();
    }

    public TaskDTO findTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow();
        return new TaskDTO(task);
    }

    @Transactional
    public TaskDTO editTask(Long id, Map<String, Object> updates) {
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            Field field = Field.valueOf(entry.getKey().toUpperCase().replace(" ", "_"));
            switch (field) {
                case NAME -> taskRepository.updateName(id, entry.getValue().toString());
                case DESCRIPTION -> taskRepository.updateDescription(id, entry.getValue().toString());
                case DEADLINE -> taskRepository.updateDeadline(id, LocalDate.parse(entry.getValue().toString()));
                case STATUS -> taskRepository.updateStatus(id, Status.valueOf(entry.getValue().toString().toUpperCase().replace(" ", "_")));
            }
        }
        Task task = taskRepository.findById(id).orElseThrow();
        return new TaskDTO(task);
    }
}
