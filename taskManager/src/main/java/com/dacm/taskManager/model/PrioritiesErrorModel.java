package com.dacm.taskManager.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrioritiesErrorModel {

    String name;
    String errorDescription;

    public PrioritiesErrorModel(String name, String errorDescription) {
        this.name = name;
        this.errorDescription = errorDescription;
    }
}
