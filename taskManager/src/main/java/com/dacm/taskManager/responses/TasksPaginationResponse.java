package com.dacm.taskManager.responses;

import com.dacm.taskManager.dto.TasksDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class TasksPaginationResponse {

    private List<TasksDTO> tasks;
    private long totalElements;
    private int totalPages;
    private int numberOfElements;
    private int size;
    private int number;

}
