package com.example.springpet.service;

import com.example.springpet.model.Status;
import com.example.springpet.model.Task;
import com.example.springpet.model.TaskDTO;
import com.example.springpet.model.exceptions.InvalidArgumentException;
import com.example.springpet.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    List<Task> listOfTasks;
    Task task1;
    Task task2;
    Task task3;
    Task task4;

    TaskDTO task1DTO;
    TaskDTO task2DTO;
    TaskDTO task3DTO;
    TaskDTO task4DTO;
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setup() {
        task1 = new Task(1L, "a", "desc1", LocalDate.parse("2022-03-12"), Status.DONE);
        task2 = new Task(2L, "b", "desc2", LocalDate.parse("2023-04-07"), Status.DONE);
        task3 = new Task(3L, "c", "desc3", LocalDate.parse("2021-02-09"), Status.TODO);
        task4 = new Task(4L, "d", "desc4", LocalDate.parse("2002-03-12"), Status.IN_PROGRESS);
        listOfTasks = new ArrayList<>(Arrays.asList(task1, task2, task3));

        task1DTO = new TaskDTO(task1);
        task2DTO = new TaskDTO(task2);
        task3DTO = new TaskDTO(task3);
        task4DTO = new TaskDTO(task4);
    }

    @Test
    void findAllTasksTest() {
        when(taskRepository.findAll()).thenReturn(listOfTasks);
        List<TaskDTO> expected = new ArrayList<>(Arrays.asList(task1DTO, task2DTO, task3DTO));

        List<TaskDTO> result = taskService.findAllTasks();
        assertEquals(expected, result);
    }

    @Test
    void saveTaskTest() {
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            listOfTasks.add(task4);
            return task4;
        });
        List<Task> expected = new ArrayList<>(Arrays.asList(task1, task2, task3, task4));

        TaskDTO result = taskService.saveTask(task4DTO);

        assertEquals(task4DTO, result);
        assertEquals(listOfTasks, expected);
    }

    @Test
    void deleteTaskTest() {
        doAnswer(invocation -> {
            listOfTasks.remove(task3);
            return null;
        }).when(taskRepository).deleteById(3L);
        List<Task> expected = Arrays.asList(task1, task2);

        taskRepository.deleteById(3L);

        assertEquals(listOfTasks, expected);
    }

    @Test
    void filterAllTasksTest() {
        when(taskRepository.findAll()).thenReturn(listOfTasks);
        List<TaskDTO> expected = Arrays.asList(task1DTO, task2DTO);

        List<TaskDTO> result = taskService.filterAllTasks(Status.DONE);

        assertEquals(expected, result);
    }

    @Test
    void sortAllTasksTest() {
        when(taskRepository.findAll()).thenReturn(listOfTasks);
        List<TaskDTO> expected = Arrays.asList(task3DTO, task1DTO, task2DTO);

        List<TaskDTO> result = taskService.sortAllTasks();

        assertEquals(expected, result);
    }

    @Test
    void findTaskByIdTest_whenExists() {
        when(taskRepository.findById(3L)).thenReturn(Optional.ofNullable(task3));

        TaskDTO result = taskService.findTaskById(3L);

        assertEquals(task3DTO, result);
    }

    @Test
    void findTaskByIdTest_whenNotExists() {
        when(taskRepository.findById(4L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskService.findTaskById(4L));
    }

    @Test
    void editTaskTest_shouldChangeName() {
        doAnswer(invocation -> {
            task3.setName("new name");
            return null;
        }).when(taskRepository).updateName(3L, "new name");
        when(taskRepository.findById(3L)).thenReturn(Optional.ofNullable(task3));

        Map<String, Object> myMap = new HashMap<>();
        myMap.put("name", "new name");
        taskService.editTask(3L, myMap);
        String result = taskService.findTaskById(3L).getName();

        assertEquals("new name", result);
    }

    @Test
    void editTaskTest_shouldChangeDescription() {
        doAnswer(invocation -> {
            task3.setDescription("new description");
            return null;
        }).when(taskRepository).updateDescription(3L, "new description");
        when(taskRepository.findById(3L)).thenReturn(Optional.ofNullable(task3));

        Map<String, Object> myMap = new HashMap<>();
        myMap.put("description", "new description");
        taskService.editTask(3L, myMap);
        String result = taskService.findTaskById(3L).getDescription();

        assertEquals("new description", result);
    }

    @Test
    void editTaskTest_shouldChangeDeadline() {
        doAnswer(invocation -> {
            task3.setDeadline(LocalDate.parse("2005-03-12"));
            return null;
        }).when(taskRepository).updateDeadline(3L, LocalDate.parse("2005-03-12"));
        when(taskRepository.findById(3L)).thenReturn(Optional.ofNullable(task3));

        Map<String, Object> myMap = new HashMap<>();
        myMap.put("deadline", LocalDate.parse("2005-03-12"));
        taskService.editTask(3L, myMap);
        LocalDate result = taskService.findTaskById(3L).getDeadline();

        assertEquals(LocalDate.parse("2005-03-12"), result);
    }

    @Test
    void editTaskTest_shouldChangeStatus() {
        doAnswer(invocation -> {
            task3.setStatus(Status.IN_PROGRESS);
            return null;
        }).when(taskRepository).updateStatus(3L, Status.IN_PROGRESS);
        when(taskRepository.findById(3L)).thenReturn(Optional.ofNullable(task3));

        Map<String, Object> myMap = new HashMap<>();
        myMap.put("status", Status.IN_PROGRESS);
        taskService.editTask(3L, myMap);
        Status result = taskService.findTaskById(3L).getStatus();

        assertEquals(Status.IN_PROGRESS, result);
    }

    @Test
    void editTaskTest_shouldThrowException() {
        Map<String, Object> myMap = new HashMap<>();
        myMap.put("fieldThatDoesNotExist", Status.IN_PROGRESS);
        assertThrows(InvalidArgumentException.class, () -> taskService.editTask(3L, myMap));
    }

    @Test
    void editTaskTest_shouldChangeAll() {
        doAnswer(invocation -> {
            task3.setName("new name");
            return null;
        }).when(taskRepository).updateName(3L, "new name");

        doAnswer(invocation -> {
            task3.setDescription("new description");
            return null;
        }).when(taskRepository).updateDescription(3L, "new description");

        doAnswer(invocation -> {
            task3.setDeadline(LocalDate.parse("2005-03-12"));
            return null;
        }).when(taskRepository).updateDeadline(3L, LocalDate.parse("2005-03-12"));

        doAnswer(invocation -> {
            task3.setStatus(Status.IN_PROGRESS);
            return null;
        }).when(taskRepository).updateStatus(3L, Status.IN_PROGRESS);
        when(taskRepository.findById(3L)).thenReturn(Optional.ofNullable(task3));

        when(taskRepository.findById(3L)).thenReturn(Optional.ofNullable(task3));


        Map<String, Object> myMap = new HashMap<>();
        myMap.put("name", "new name");
        myMap.put("description", "new description");
        myMap.put("deadline", LocalDate.parse("2005-03-12"));
        myMap.put("status", Status.IN_PROGRESS);
        taskService.editTask(3L, myMap);
        String resultName = taskService.findTaskById(3L).getName();
        String resultDescription = taskService.findTaskById(3L).getDescription();
        LocalDate resultDeadline = taskService.findTaskById(3L).getDeadline();
        Status resultStatus = taskService.findTaskById(3L).getStatus();


        assertEquals("new name", resultName);
        assertEquals("new description", resultDescription);
        assertEquals(LocalDate.parse("2005-03-12"), resultDeadline);
        assertEquals(Status.IN_PROGRESS, resultStatus);
    }
}
