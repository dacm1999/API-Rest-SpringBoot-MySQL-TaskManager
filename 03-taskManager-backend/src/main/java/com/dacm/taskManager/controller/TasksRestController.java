package com.dacm.taskManager.controller;

import com.dacm.taskManager.dto.TasksDTO;
import com.dacm.taskManager.entity.Tasks;
import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.responses.AddedResponse;
import com.dacm.taskManager.responses.TasksErrorResponse;
import com.dacm.taskManager.repository.TaskRepository;
import com.dacm.taskManager.repository.UserRepository;
import com.dacm.taskManager.responses.TasksByUsernameResponse;
import com.dacm.taskManager.responses.TasksPaginationResponse;
import com.dacm.taskManager.service.implementedService.PrioritiesServiceImpl;
import com.dacm.taskManager.service.implementedService.TasksServiceImpl;
import com.dacm.taskManager.service.implementedService.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/tasks")
public class TasksRestController {

    private final TasksServiceImpl tasksService;

    @Operation(summary = "Add a single task")
    @ApiResponse(responseCode = "200", description = "Task added successfully")
    @PostMapping(value = "/create")
    public ResponseEntity<?> addSingleTask(@RequestBody Tasks tasks) {
        TasksDTO tasksDTO = tasksService.addSingleTask(tasks);
        if (tasksDTO != null) {
            return ResponseEntity.ok(tasksDTO);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Add multiple tasks")
    @ApiResponse(responseCode = "200", description = "Tasks added successfully")
    @PostMapping("/createMany")
    public ResponseEntity<?> addManyTasks(@RequestBody Tasks[] tasks) {
        AddedResponse response = tasksService.addManyTasks(tasks);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Get task by ID")
    @ApiResponse(responseCode = "200", description = "Task found successfully")
    @GetMapping(value = "info/{id}")
    public ResponseEntity<?> getTasksById(@PathVariable Integer id) {
        try {
            TasksDTO tasksDTO = tasksService.getById(id);
            return ResponseEntity.ok(tasksDTO);
        } catch (NoSuchElementException ex) {
            throw new CommonErrorResponse("We could not found tasks by ID " + id, ex);
        }
    }

    @Operation(summary = "Get all tasks")
    @ApiResponse(responseCode = "200", description = "All tasks found successfully")
    @GetMapping(value = "/all")
    public ResponseEntity<?> showAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDate creation_date,
            @RequestParam(required = false) LocalDate due_date,
            @RequestParam(required = false) String priority
    ) {
        Page<TasksDTO> result = tasksService.getAllTask(PageRequest.of(page, size), name, description, status, creation_date, due_date, priority);
        TasksPaginationResponse response = tasksService.createPaginationResponse(result);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Get all tasks by username")
    @ApiResponse(responseCode = "200", description = "All tasks found successfully")
    @GetMapping(value = "/usertasks/{username}")
    public ResponseEntity<?> getTasksWithDetails
            (
                    @PathVariable String username,
                    @RequestParam(defaultValue = "0") int page,
                    @RequestParam(defaultValue = "10") int size,
                    @RequestParam(required = false) String name,
                    @RequestParam(required = false) String description,
                    @RequestParam(required = false) String status,
                    @RequestParam(required = false) LocalDate creation_date,
                    @RequestParam(required = false) LocalDate due_date,
                    @RequestParam(required = false) String priority
            ) {
        try {
            TasksByUsernameResponse response = tasksService.getTasksByUsername(username, page, size, name, description, status, creation_date, due_date, priority);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            throw new CommonErrorResponse("User not found with name: " + username);
        }
    }


    @Operation(summary = "Get all tasks by user ID")
    @ApiResponse(responseCode = "200", description = "All tasks found successfully")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUserId(@PathVariable int userId) {
        List<TasksDTO> tasksDTOList = tasksService.getByUserId(userId);
        return ResponseEntity.ok(tasksDTOList);
    }


    @Operation(summary = "Update task by ID")
    @ApiResponse(responseCode = "200", description = "Task updated successfully")
    @PutMapping(value = "update/{id}")
    public ResponseEntity<?> updatedById(@PathVariable Integer id, @RequestBody TasksDTO updatedDTO) {
        try {
            TasksDTO updatedTaskDTO = tasksService.updateById(id, updatedDTO);
            return ResponseEntity.ok(updatedTaskDTO);
        } catch (NoSuchElementException e) {
            throw new CommonErrorResponse("Task not found with ID: " + id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new CommonErrorResponse(e.getMessage(), e);
        }
    }


    @Operation(summary = "Delete task by ID")
    @ApiResponse(responseCode = "200", description = "Task deleted successfully")
    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        try {
            TasksDTO deletedTaskDTO = tasksService.deleteById(id);
            return ResponseEntity.ok(deletedTaskDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
