package com.dacm.taskManager.controller;

import com.dacm.taskManager.dto.TasksDTO;
import com.dacm.taskManager.entity.Tasks;
import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.model.AddModel;
import com.dacm.taskManager.model.TasksErrorModel;
import com.dacm.taskManager.repository.TaskRepository;
import com.dacm.taskManager.responses.TasksPaginationResponse;
import com.dacm.taskManager.service.implementService.TasksServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TasksRestController {

    @Autowired
    private final TaskRepository tasksRepository;

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
    public ResponseEntity<TasksPaginationResponse> showAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String status,
            @RequestParam(required = false)LocalDate creation_date,
            @RequestParam(required = false) LocalDate due_date,
            @RequestParam(required = false) String priority
            ){

        Page<TasksDTO> tasksDTOPage = tasksService.getAllTask(
                PageRequest.of(page,size),
                name,
                description,
                status,
                creation_date,
                due_date,
                priority
        );

        TasksPaginationResponse response = new TasksPaginationResponse();
        response.setTasks(tasksDTOPage.getContent());
        response.setTotalPages(tasksDTOPage.getTotalPages());
        response.setTotalElements(tasksDTOPage.getTotalElements());
        response.setNumber(tasksDTOPage.getNumber());
        response.setNumberOfElements(tasksDTOPage.getNumberOfElements());
        response.setSize(tasksDTOPage.getSize());

        return ResponseEntity.ok(response);
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


    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TasksDTO> deleteById(@PathVariable Integer id){
        try{
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
    public ResponseEntity<?> addSingleTask(@RequestBody Tasks tasks){
        tasksDTOList = tasksService.getAllTasks();
        List<TasksErrorModel> failed = new ArrayList<>();
        String userId = String.valueOf(tasks.getUser_id());
        String reason = "No se encontró motivo"; // Valor predeterminado

        boolean repeatName = false;
        for(TasksDTO tasksDTO1 : tasksDTOList){
            if(tasks.getName().equals(tasksDTO1.getName())){
                repeatName = true;
                reason = "COULD NOT ADD TASK BECAUSE NAME IS DUPLICATED";
                break;
            } else if (userId.equals(null) || userId.equals(" ")) {
                reason = "COULD NOT ADD TASK BECAUSE USER ID IS NULL OR EMPTY";
                break;
            }
        }

        TasksErrorModel errorModel = new TasksErrorModel(tasks.getName(), tasks.getUser_id(), reason);
        failed.add(errorModel);

        if (!repeatName){
            Tasks saveTasks = tasksRepository.save(tasks);
            tasksDTO = tasksService.convertToDTO(saveTasks);
            return ResponseEntity.ok(tasksDTO);
        } else {
            return ResponseEntity.ok(failed);
        }
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AddModel> addManyTasks(@RequestBody Tasks[] tasks) {
        List<TasksDTO> tasksDTOList = tasksService.getAllTasks();
        AddModel result;
        String reason = "";
        int total = tasks.length;
        int count_added = 0;
        int count_failed = 0;
        List<TasksErrorModel> failed = new ArrayList<>();
        List<Tasks> addedTags = new ArrayList<>();

        for (Tasks tempTasks : tasks) {
            boolean duplicateName = false;
            for (TasksDTO tempTasksDTO : tasksDTOList) {
                if (tempTasksDTO.getName().equals(tempTasks.getName())) {
                    TasksErrorModel tasksErrorModel = new TasksErrorModel(
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

        result = new AddModel(true, total, count_added, count_failed, (ArrayList<Tasks>) addedTags, (ArrayList<TasksErrorModel>) failed, reason);

        return ResponseEntity.ok(result);
    }


}
