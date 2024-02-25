package com.dacm.taskManager.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TasksErrorModel {

    String name;
    int userId;
    String errorDescription;

    public TasksErrorModel(String name, int userId, String errorDescription) {
        this.name = name;
        this.userId = userId;
        this.errorDescription = errorDescription;
    }
}

