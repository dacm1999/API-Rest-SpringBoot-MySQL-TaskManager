package com.dacm.taskManager.controller;

import com.dacm.taskManager.repository.TasksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TasksRestController {

    private final TasksRepository tasksRepository;


}
