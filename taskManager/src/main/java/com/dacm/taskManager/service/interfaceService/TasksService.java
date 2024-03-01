package com.dacm.taskManager.service.interfaceService;

import com.dacm.taskManager.dto.TasksDTO;
import com.dacm.taskManager.entity.Tasks;
import com.dacm.taskManager.responses.TasksByUsernameResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TasksService {

    TasksDTO convertToDTO(Tasks tasks);
    TasksDTO getById(int id);
    List<TasksDTO> getByUserId(int id);
    List<TasksDTO> getAllTasks();
    Page<TasksDTO> getAllTask(PageRequest pageRequest, String name, String description, String status, LocalDate creation_date, LocalDate due_date, String priority);
    Page<TasksDTO> getAllTasksByUserId(int userId, PageRequest pageRequest, String name, String description, String status, LocalDate creation_date, LocalDate due_date, String priority);
    TasksDTO updateById(Integer id, TasksDTO tasksDTO);
    TasksDTO deleteById(Integer id);
    void saveManyTasks(Tasks tasks);


}
