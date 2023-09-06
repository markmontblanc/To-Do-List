package com.softserve.itacademy.todolist.dto;

public class ToDoDto {

    private String title;

    public ToDoDto() {
    }

    public ToDoDto(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
