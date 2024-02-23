package com.dacm.taskManager.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserErrorModel {
    String username;
    String email;
    String errorDescription;

    public UserErrorModel(String username, String email, String errorDescription) {
        this.username = username;
        this.email = email;
        this.errorDescription = errorDescription;
    }
}
