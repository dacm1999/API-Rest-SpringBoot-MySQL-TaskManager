package com.dacm.taskManager.dto;

import com.dacm.taskManager.enums.Status;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TasksDTO {

    String name;
    String description;
    String status;
    LocalDate creation_date;
    LocalDate due_date;
    String priority;

}
