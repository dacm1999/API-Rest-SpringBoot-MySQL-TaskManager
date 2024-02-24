package com.dacm.taskManager.controller;

import com.dacm.taskManager.dto.TasksDTO;
import com.dacm.taskManager.entity.Tasks;
import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.repository.TasksRepository;
import com.dacm.taskManager.service.impl.TasksServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TasksRestController {

    @Autowired
    private final TasksRepository tasksRepository;

    @Autowired
    private final TasksServiceImpl tasksService;
    private Tasks tasks;
    private TasksDTO tasksDTO;
    private List<Tasks> tasksList;
    private List<TasksDTO> tasksDTOList;

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<TasksDTO> getTasksById(@PathVariable Integer id) {
        try {
            tasksDTO = tasksService.getById(id);
            return ResponseEntity.ok(tasksDTO);
        } catch (NoSuchElementException ex) {
            throw new CommonErrorResponse("We could not found tasks by ID " + id, ex);
        }
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<TasksDTO>> getAllTasks(){
        tasksDTOList = tasksService.getAllTasks();
        return ResponseEntity.ok(tasksDTOList);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<TasksDTO> updatedById(@PathVariable Integer id, @RequestBody TasksDTO updatedDTO){

        try{
            tasksDTO = tasksService.updateById(id,updatedDTO);
            return ResponseEntity.ok(updatedDTO);
        } catch (NoSuchElementException e) {
            throw new CommonErrorResponse("Task not found with ID: " + id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new CommonErrorResponse(e.getMessage(), e);
        }
    }
}
