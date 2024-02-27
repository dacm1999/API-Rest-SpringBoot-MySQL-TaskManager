package com.dacm.taskManager.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagsErrorResponse {

    String name;
    String errorDescription;

    public TagsErrorResponse(String name, String errorDescription) {
        this.name = name;
        this.errorDescription = errorDescription;
    }

}
