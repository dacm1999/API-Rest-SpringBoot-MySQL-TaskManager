package com.dacm.taskManager.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagsErrorModel {

    String name;
    String errorDescription;

    public TagsErrorModel(String name, String errorDescription) {
        this.name = name;
        this.errorDescription = errorDescription;
    }

}
