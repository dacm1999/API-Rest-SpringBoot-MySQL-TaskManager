package com.dacm.taskManager.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TasksErrorResponse {

    String name;
    int userId;
    String errorDescription;

    public TasksErrorResponse(String name, int userId, String errorDescription) {
        this.name = name;
        this.userId = userId;
        this.errorDescription = errorDescription;
    }
}

