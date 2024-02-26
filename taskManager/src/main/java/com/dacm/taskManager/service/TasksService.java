package com.dacm.taskManager.service;

import com.dacm.taskManager.dto.TasksDTO;
import com.dacm.taskManager.dto.UserDTO;
import com.dacm.taskManager.entity.Tasks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TasksService {

    TasksDTO convertToDTO(Tasks tasks);
    TasksDTO getById(int id);
    TasksDTO geyByUserId(int id);
    List<TasksDTO> getAllTasks();
    Page<TasksDTO> getAllTask(PageRequest pageRequest, String name, String description, String status, LocalDate creation_date, LocalDate due_date, String priority);

    TasksDTO updateById(Integer id, TasksDTO tasksDTO);
    TasksDTO deleteById(Integer id);
    void saveManyTasks(Tasks tasks);


}
