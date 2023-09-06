package com.softserve.itacademy.todolist.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softserve.itacademy.todolist.model.ToDo;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class ToDoResponse {
    long id;
    String title;
    LocalDateTime created_at;
    long owner_id;

    public ToDoResponse(ToDo todo) {
        id = todo.getId();
        title = todo.getTitle();
        created_at = todo.getCreatedAt();
        owner_id = todo.getOwner().getId();
    }
}
