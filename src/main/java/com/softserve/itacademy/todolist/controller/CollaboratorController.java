package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.CollaboratorResponse;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.service.ToDoService;
import com.softserve.itacademy.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{user_id}/todos/{todo_id}/collaborators")
public class CollaboratorController {

    @Autowired
    ToDoService toDoService;

    @Autowired
    UserService userService;

    @GetMapping
    public List<CollaboratorResponse> getCollaborators(@PathVariable("todo_id") long todoId) {
        return toDoService.readById(todoId).getCollaborators().stream()
                .map(CollaboratorResponse::new)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<User> addCollaborator(@PathVariable("user_id") long userId, @PathVariable("todo_id") long todoId) {
        List<User> collaborators = toDoService.readById(todoId).getCollaborators();
        boolean contains = collaborators.contains(userService.readById(userId));
        if (!contains) {
            collaborators.add(userService.readById(userId));
            toDoService.readById(todoId).setCollaborators(collaborators);
            toDoService.update(toDoService.readById(todoId));
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<User> deleteCollaborator(@PathVariable("user_id") long userId, @PathVariable("todo_id") long todoId) {
        List<User> collaborators = toDoService.readById(todoId).getCollaborators();
        boolean contains = collaborators.contains(userService.readById(userId));
        if (!contains) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            collaborators.remove(userService.readById(userId));
            toDoService.readById(todoId).setCollaborators(collaborators);
            toDoService.update(toDoService.readById(todoId));
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }
}
