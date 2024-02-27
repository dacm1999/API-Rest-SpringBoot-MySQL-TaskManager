package com.dacm.taskManager.service.implementedService;

import com.dacm.taskManager.dto.TasksDTO;
import com.dacm.taskManager.entity.Tasks;
import com.dacm.taskManager.enums.Status;
import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.repository.TaskRepository;
import com.dacm.taskManager.service.interfaceService.TasksService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TasksServiceImpl implements TasksService {

    @Autowired
    private final TaskRepository tasksRepository;
    private Tasks tasks;
    private TasksDTO dto;
    private List<Tasks> tasksList;
    private List<TasksDTO> tasksDTOList;


    @Override
    public TasksDTO convertToDTO(Tasks tasks) {
        dto = new TasksDTO();
        dto.setName(tasks.getName());
        dto.setDescription(tasks.getDescription());
        dto.setStatus(String.valueOf(tasks.getStatus()));
        dto.setCreation_date(tasks.getCreation_date());
        dto.setDue_date(tasks.getDue_date());
        dto.setPriority(String.valueOf(tasks.getPriority_id()));
        return dto;
    }

    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);
    }

    @Override
    public TasksDTO getById(int id) {
        tasks = tasksRepository.findById(id).orElseThrow();
        if (tasks != null) {
            dto = new TasksDTO();
            dto.setName(tasks.getName());
            dto.setDescription(tasks.getDescription());
            dto.setStatus(String.valueOf(tasks.getStatus()));
            dto.setCreation_date(tasks.getCreation_date());
            dto.setDue_date(tasks.getDue_date());
            dto.setPriority(String.valueOf(tasks.getPriority_id()));
            return dto;
        }
        return null;
    }

    @Override
    public List<TasksDTO> getByUserId(int user_id) {
        List<TasksDTO> tasksDTOList = new ArrayList<>();

        // Buscar tareas por user_id
        List<Tasks> tasksList = tasksRepository.findByUserId(user_id);

        // Verificar si se encontraron tareas
        if (!tasksList.isEmpty()) {
            for (Tasks tasks : tasksList) {
                TasksDTO dto = new TasksDTO();
                dto.setName(tasks.getName());
                dto.setDescription(tasks.getDescription());
                dto.setStatus(String.valueOf(tasks.getStatus()));
                dto.setCreation_date(tasks.getCreation_date());
                dto.setDue_date(tasks.getDue_date());
                dto.setPriority(String.valueOf(tasks.getPriority_id()));
                tasksDTOList.add(dto);
            }
        } else {
            throw new CommonErrorResponse("COULD NOT FIND TASKS WITH USER ID: " + user_id);
        }

        return tasksDTOList;
    }

    @Override
    public List<TasksDTO> getAllTasks() {
        tasksList = tasksRepository.findAll();
        tasksDTOList = new ArrayList<>();

        for (Tasks tempTasks : tasksList) {
            dto = convertToDTO(tempTasks);
            tasksDTOList.add(dto);
        }
        return tasksDTOList;
    }

    @Override
    public Page<TasksDTO> getAllTask(PageRequest pageRequest, String name, String description, String status, LocalDate creation_date, LocalDate due_date, String priority) {
        Specification<Tasks> specification = Specification.where(null);
        if (name != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name));
        }
        if (description != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("description"), description));
        }
        if (status != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status));
        }
        if (creation_date != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("creation_date"), creation_date));
        }
        if (due_date != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("due_date"), due_date));
        }
        if (priority != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("priority"), priority));
        }

        Page<Tasks> tasksPage = tasksRepository.findAll(specification,pageRequest);
        return tasksPage.map(this::convertToDTO);
    }

    @Override
    public TasksDTO updateById(Integer id, TasksDTO tasksDTO) {
        tasks = tasksRepository.findById(id).orElseThrow();
        tasks.setName(tasksDTO.getName());
        tasks.setDescription(tasksDTO.getDescription());
        tasks.setStatus(Status.valueOf(tasksDTO.getStatus()));

        // Trim the date string provided by the user and then parse it to LocalDate
        tasks.setCreation_date(tasksDTO.getCreation_date());
        tasks.setDue_date(tasksDTO.getDue_date());
        tasks.setPriority_id(Integer.parseInt(tasksDTO.getPriority()));
        Tasks updated = tasksRepository.save(tasks);

        return convertToDTO(updated);
    }

    @Override
    public TasksDTO deleteById(Integer id) {
        tasks = tasksRepository.findById(id).orElseThrow();
        tasksRepository.deleteById(id);
        return convertToDTO(tasks);
    }

    @Override
    public void saveManyTasks(Tasks tasks) {
        Optional<Tasks> existingTasks = tasksRepository.findById(tasks.getId());
        if (!existingTasks.isPresent()) {
            tasksRepository.save(tasks);
        }

    }

}
