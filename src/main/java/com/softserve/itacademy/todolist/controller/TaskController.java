package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.TaskDto;
import com.softserve.itacademy.todolist.dto.TaskResponse;
import com.softserve.itacademy.todolist.dto.TaskTransformer;
import com.softserve.itacademy.todolist.model.Task;
import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.service.StateService;
import com.softserve.itacademy.todolist.service.TaskService;
import com.softserve.itacademy.todolist.service.ToDoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/api/users/{user_id}/todos/{todo_id}/tasks")
public class TaskController {

    private final TaskService taskService;
    private final ToDoService toDoService;
    private final StateService stateService;

    @Autowired
    public TaskController(TaskService taskService, ToDoService toDoService, StateService stateService) {
        this.taskService = taskService;
        this.toDoService = toDoService;
        this.stateService = stateService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TaskResponse> create(@PathVariable(value = "todo_id") Long toDoId, @RequestBody TaskDto taskDto) {
        ToDo toDo = toDoService.readById(toDoId);
        if (toDo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Task task = TaskTransformer.convertToEntity(taskDto, toDo, stateService.getByName("NEW"));
        Task newTask = taskService.create(task);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "api/tasks/" + newTask.getId())
                .body(new TaskResponse(newTask));
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TaskResponse> read(@PathVariable(value = "id") Long id) {
        Task task = taskService.readById(id);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new TaskResponse(task));
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Task> update(@PathVariable(value = "id") Long id, @RequestBody Task task) {
        task.setId(id);
        taskService.update(task);

        return ResponseEntity.status(HttpStatus.OK)
                .body(task);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Task> delete(@PathVariable(value = "id") Long id) {
        Task existingTask = taskService.readById(id);
        if (existingTask != null) {
            taskService.delete(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<TaskResponse>> getAll() {
        List<TaskResponse> taskResponses = taskService.getAll().stream()
                .map(TaskResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(taskResponses);
    }
}
