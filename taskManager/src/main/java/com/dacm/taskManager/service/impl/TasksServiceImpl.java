package com.dacm.taskManager.service.impl;

import com.dacm.taskManager.dto.TasksDTO;
import com.dacm.taskManager.entity.Tasks;
import com.dacm.taskManager.enums.Status;
import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.repository.TasksRepository;
import com.dacm.taskManager.service.TasksService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.tomcat.util.http.FastHttpDateFormat.formatDate;

@Service
@RequiredArgsConstructor
public class TasksServiceImpl implements TasksService {

    @Autowired
    private final TasksRepository tasksRepository;
    private Tasks tasks;
    private TasksDTO dto;
    private List<Tasks> tasksList;
    private List<TasksDTO> tasksDTOList;

    @Override
    public String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return dateTime.format(formatter);
    }

    @Override
    public TasksDTO convertToDTO(Tasks tasks) {
        dto = new TasksDTO();
        dto.setName(tasks.getName());
        dto.setDescription(tasks.getDescription());
        dto.setStatus(String.valueOf(tasks.getStatus()));

        // Formatear la fecha de creación
        LocalDateTime creationDateTime = LocalDateTime.parse(tasks.getCreation_date(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        dto.setCreation_date(formatDateTime(creationDateTime));

        // Formatear la fecha de vencimiento
        LocalDate dueDate = LocalDate.parse(tasks.getDue_date(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        dto.setDue_date(formatDate(dueDate)); // Utilizamos un método diferente para formatear solo la fecha

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
            LocalDateTime creationDateTime = LocalDateTime.parse(tasks.getCreation_date(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            dto.setCreation_date(formatDateTime(creationDateTime));
            LocalDateTime dueDateTime = LocalDateTime.parse(tasks.due_date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dto.setDue_date(formatDateTime(dueDateTime));
            dto.setPriority(String.valueOf(tasks.getPriority_id()));
            return dto;
        }
        return null;
    }

    @Override
    public TasksDTO geyByUserId(int user_id) {
        // Buscar tareas por user_id
        tasks = (Tasks) tasksRepository.findByUserId(user_id);

        // Verificar si se encontraron tareas
        if (tasks != null) {
            dto = new TasksDTO();
            dto.setName(tasks.getName());
            dto.setDescription(tasks.getDescription());
            dto.setStatus(String.valueOf(tasks.getStatus()));
            dto.setCreation_date(tasks.getCreation_date().format(String.valueOf(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            dto.setDue_date(tasks.getDue_date());
            dto.setPriority(String.valueOf(tasks.getPriority_id()));
            return dto;
        } else {
            throw new CommonErrorResponse("COULD NOT FOUND TASKS WITH USER ID: " + user_id);
        }
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
    public TasksDTO updateById(Integer id, TasksDTO tasksDTO) {
        tasks = tasksRepository.findById(id).orElseThrow();
        tasks.setName(tasksDTO.getName());
        tasks.setDescription(tasksDTO.getDescription());
        tasks.setStatus(Status.valueOf(tasksDTO.getStatus()));

        // Parseamos la fecha ingresada por el usuario al formato LocalDate
        LocalDate creationDate = LocalDate.parse(tasksDTO.getCreation_date(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // Creamos un objeto LocalDateTime con la fecha y la hora predeterminada (medianoche)
        LocalDateTime creationDateTime = creationDate.atStartOfDay();
        // Formateamos la fecha y hora para que coincida con el formato deseado
        tasks.setCreation_date(formatDateTime(creationDateTime));
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
        if(existingTasks == null){
            tasksRepository.save(tasks);
        }

    }

}
