package com.dacm.taskManager.service.interfaceService;

import com.dacm.taskManager.dto.TasksDTO;
import com.dacm.taskManager.entity.Tasks;
import com.dacm.taskManager.responses.AddedResponse;
import com.dacm.taskManager.responses.TasksByUsernameResponse;
import com.dacm.taskManager.responses.TasksPaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public interface TasksService {

    TasksDTO addSingleTask(Tasks tasks);
    AddedResponse addManyTasks(Tasks[] tasks);
    void saveManyTasks(Tasks tasks);
    TasksDTO getById(int id);
    List<TasksDTO> getByUserId(int id);
    List<TasksDTO> getAllTasks();
    Page<TasksDTO> getAllTask(PageRequest pageRequest, String name, String description, String status, LocalDate creation_date, LocalDate due_date, String priority);
    TasksPaginationResponse createPaginationResponse(Page<TasksDTO> result);
    Page<TasksDTO> getAllTasksByUserId(int userId, PageRequest pageRequest, String name, String description, String status, LocalDate creation_date, LocalDate due_date, String priority);
    TasksByUsernameResponse getTasksByUsername(String username, int page, int size, String name, String description, String status, LocalDate creation_date, LocalDate due_date, String priority);
    TasksByUsernameResponse createTasksByUsernameResponse(String username, Page<TasksDTO> result, int totalTasks, int count_completed, int count_pending);
    TasksDTO updateById(Integer id, TasksDTO tasksDTO);
    TasksDTO deleteById(Integer id);
    TasksDTO convertToDTO(Tasks tasks);


}
