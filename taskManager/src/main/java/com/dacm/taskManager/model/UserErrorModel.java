package com.dacm.taskManager.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserErrorModel {
    String username;
    String email;

    public UserErrorModel(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
