package com.dacm.taskManager.dto;

import com.dacm.taskManager.enums.Status;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class TasksDTO {

    String name;
    String description;
    String status;
    String creation_date;
    String due_date;
    String priority;

}
