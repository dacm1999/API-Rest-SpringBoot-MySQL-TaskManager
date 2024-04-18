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
@RequestMapping("/api/v1/tasks")
public class TasksRestController {

    private final TaskRepository tasksRepository;
    private final TasksServiceImpl tasksService;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final PrioritiesServiceImpl prioritiesService;
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
    public ResponseEntity<TasksPaginationResponse> showAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDate creation_date,
            @RequestParam(required = false) LocalDate due_date,
            @RequestParam(required = false) String priority
    ) {

        Page<TasksDTO> result = tasksService.getAllTask(
                PageRequest.of(page, size),
                name,
                description,
                status,
                creation_date,
                due_date,
                priority
        );

        TasksPaginationResponse response = new TasksPaginationResponse();
        response.setTasks(result.getContent());
        response.setTotalPages(result.getTotalPages());
        response.setTotalElements(result.getTotalElements());
        response.setPage(result.getNumber());
        response.setSize(result.getSize());
        response.setNumberOfElements(result.getNumberOfElements());

        return ResponseEntity.ok(response);
    }


    @GetMapping(value = "/userTasks/{username}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<TasksByUsernameResponse> getTasksWithDetails(@PathVariable String username,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size,
                                                                       @RequestParam(required = false) String name,
                                                                       @RequestParam(required = false) String description,
                                                                       @RequestParam(required = false) String status,
                                                                       @RequestParam(required = false) LocalDate creation_date,
                                                                       @RequestParam(required = false) LocalDate due_date,
                                                                       @RequestParam(required = false) String priority) {


        try{
            Optional<User> user = userRepository.findByUsername(username);
            int totalTasks = 0;
            int count_completed = 0;
            int count_pending = 0;
            List<TasksDTO> completedTasks = new ArrayList<>();
            List<TasksDTO> pendingTasksList = new ArrayList<>();

            int userId = user.get().userId;

            Page<TasksDTO> result = tasksService.getAllTasksByUserId(
                    userId,
                    PageRequest.of(page, size),
                    name,
                    description,
                    status,
                    creation_date,
                    due_date,
                    priority
            );
            List<TasksDTO> tasksDTOList = tasksService.getByUserId(userId);

            for(TasksDTO tempDTO : result.getContent()){
                if(tempDTO.getStatus().equalsIgnoreCase("Pending")){
                    count_pending++;
                    pendingTasksList.add(tempDTO);
                }
                if(tempDTO.getStatus().equalsIgnoreCase("Completed")){
                    count_completed++;
                    completedTasks.add(tempDTO);
                }

                totalTasks++;
            }


            Page<TasksDTO> completedTasksPage = new PageImpl<>(completedTasks, result.getPageable(), completedTasks.size());
            Page<TasksDTO> pendingTasksPage = new PageImpl<>(pendingTasksList, result.getPageable(), pendingTasksList.size());



            TasksByUsernameResponse response = new TasksByUsernameResponse();
            response.setUsername(username);
            response.setTotalTasks(totalTasks);
            response.setTotalCompletedTasks(count_completed);
            response.setTotalPendingTasks(count_pending);
            response.setCompletedTasks(completedTasksPage.getContent());
            response.setPendingTasks(pendingTasksPage.getContent());
            response.setNumberOfElements(tasksDTOList.size());
            response.setTotalPages(result.getTotalPages());
            response.setTotalElements(tasksDTOList.size());
            response.setPage(result.getNumber());
            response.setSize(result.getSize());

            return ResponseEntity.ok(response);
        }catch (NoSuchElementException e){
            throw new CommonErrorResponse("User not found with name: " + username);
        }

    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TasksDTO>> getByUserId(@PathVariable int userId) {
        List<TasksDTO> tasksDTOList = tasksService.getByUserId(userId);
        return ResponseEntity.ok(tasksDTOList);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<TasksDTO> updatedById(@PathVariable Integer id, @RequestBody TasksDTO updatedDTO) {

        try {
            tasksDTO = tasksService.updateById(id, updatedDTO);
            return ResponseEntity.ok(updatedDTO);
        } catch (NoSuchElementException e) {
            throw new CommonErrorResponse("Task not found with ID: " + id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new CommonErrorResponse(e.getMessage(), e);
        }
    }


    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TasksDTO> deleteById(@PathVariable Integer id) {
        try {
            tasksDTO = tasksService.deleteById(id);
            return ResponseEntity.ok(tasksDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/single")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> addSingleTask(@RequestBody Tasks tasks) {
        tasksDTOList = tasksService.getAllTasks();
        List<TasksErrorResponse> failed = new ArrayList<>();
        String userId = String.valueOf(tasks.getUser_id());
        String reason = "No se encontró motivo"; // Valor predeterminado

        boolean repeatName = false;
        for (TasksDTO tasksDTO1 : tasksDTOList) {
            if (tasks.getName().equals(tasksDTO1.getName())) {
                repeatName = true;
                reason = "COULD NOT ADD TASK BECAUSE NAME IS DUPLICATED";
                break;
            } else if (userId.equals(null) || userId.equals(" ")) {
                reason = "COULD NOT ADD TASK BECAUSE USER ID IS NULL OR EMPTY";
                break;
            }
        }

        TasksErrorResponse errorModel = new TasksErrorResponse(tasks.getName(), tasks.getUser_id(), reason);
        failed.add(errorModel);

        if (!repeatName) {
            Tasks saveTasks = tasksRepository.save(tasks);
            tasksDTO = tasksService.convertToDTO(saveTasks);
            return ResponseEntity.ok(tasksDTO);
        } else {
            return ResponseEntity.ok(failed);
        }
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AddedResponse> addManyTasks(@RequestBody Tasks[] tasks) {
        List<TasksDTO> tasksDTOList = tasksService.getAllTasks();
        AddedResponse result;
        String reason = "";
        int total = tasks.length;
        int count_added = 0;
        int count_failed = 0;
        List<TasksErrorResponse> failed = new ArrayList<>();
        List<Tasks> addedTags = new ArrayList<>();

        for (Tasks tempTasks : tasks) {
            boolean duplicateName = false;
            for (TasksDTO tempTasksDTO : tasksDTOList) {
                if (tempTasksDTO.getName().equals(tempTasks.getName())) {
                    TasksErrorResponse tasksErrorModel = new TasksErrorResponse(
                            tempTasks.getName(), tempTasks.getUser_id()
                            , "TASK NAME DUPLICATED " + tempTasks.getName());
                    failed.add(tasksErrorModel);
                    count_failed++;
                    duplicateName = true;
                    reason = "COULD NOT ADD THIS TASKS";
                    break;
                }
            }
            if (!duplicateName) {
                tasksService.saveManyTasks(tempTasks);
                reason = "TASKS ADDED SUCCESSFULLY";
                addedTags.add(tempTasks);
                count_added++;
            }
        }

        result = new AddedResponse(true, total, count_added, count_failed, (ArrayList<Tasks>) addedTags, (ArrayList<TasksErrorResponse>) failed, reason);

        return ResponseEntity.ok(result);
    }


}
