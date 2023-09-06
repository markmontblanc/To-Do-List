package com.softserve.itacademy.todolist.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softserve.itacademy.todolist.model.User;
import lombok.Value;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CollaboratorResponse {
    long collaborator_id;

    public CollaboratorResponse(User collaborator) {
        this.collaborator_id = collaborator.getId();
    }
}
