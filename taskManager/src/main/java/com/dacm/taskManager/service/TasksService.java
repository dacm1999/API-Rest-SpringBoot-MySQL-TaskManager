package com.dacm.taskManager.service;

import com.dacm.taskManager.dto.TasksDTO;
import com.dacm.taskManager.entity.Tasks;

import java.time.LocalDateTime;
import java.util.List;

public interface TasksService {

    String formatDateTime(LocalDateTime dateTime);
    TasksDTO convertToDTO(Tasks tasks);
    TasksDTO getById(int id);
    TasksDTO geyByUserId(int id);
    List<TasksDTO> getAllTasks();
    TasksDTO updateById(Integer id, TasksDTO tasksDTO);
    TasksDTO deleteById(Integer id);
    void saveManyTasks(Tasks tasks);


}
