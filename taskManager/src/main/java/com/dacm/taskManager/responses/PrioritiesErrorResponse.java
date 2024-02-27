package com.dacm.taskManager.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrioritiesErrorResponse {

    String name;
    String errorDescription;

    public PrioritiesErrorResponse(String name, String errorDescription) {
        this.name = name;
        this.errorDescription = errorDescription;
    }
}
