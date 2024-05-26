package com.dacm.taskManager.controller;

import com.dacm.taskManager.dto.PrioritiesDTO;
import com.dacm.taskManager.entity.Priorities;
import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.responses.AddedResponse;
import com.dacm.taskManager.responses.PrioritiesErrorResponse;
import com.dacm.taskManager.responses.TagsErrorResponse;
import com.dacm.taskManager.repository.PriorityRepository;
import com.dacm.taskManager.responses.PrioritiesPaginationResponse;
import com.dacm.taskManager.service.implementedService.PrioritiesServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/priorities")
public class PrioritiesRestController {

    @Autowired
    private final PrioritiesServiceImpl prioritiesService;

    @Operation(summary = "Add new priority")
    @ApiResponse(responseCode = "200", description = "Priority added successfully")
    @PostMapping(value = "/create")
    public ResponseEntity<?> addSingle(@RequestBody Priorities priorities) {
        return ResponseEntity.ok(prioritiesService.createPriority(priorities));
    }

    @Operation(summary = "Add multiple priorities")
    @ApiResponse(responseCode = "200", description = "Priorities added successfully")
    @PostMapping(value = "/createMany")
    public ResponseEntity<AddedResponse> addManyPriorities(@RequestBody Priorities[] priorities) {
        AddedResponse result = prioritiesService.createManyPriorities(priorities);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Retrieve a priority by name")
    @ApiResponse(responseCode = "200", description = "Priority retrieved successfully")
    @GetMapping(value = "/{name}")
    public ResponseEntity<?> getByName(@PathVariable String name) {
        PrioritiesDTO prioritiesDTO = prioritiesService.getByName(name);
        return ResponseEntity.ok(prioritiesDTO);
    }

    @Operation(summary = "Retrieve a priority by ID")
    @ApiResponse(responseCode = "200", description = "Priority retrieved successfully")
    @GetMapping(value = "/info/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        PrioritiesDTO prioritiesDTO = prioritiesService.getById(id);
        return ResponseEntity.ok(prioritiesDTO);
    }

    @Operation(summary = "Retrieve all priorities")
    @ApiResponse(responseCode = "200", description = "Priorities retrieved successfully")
    @GetMapping(value = "/all")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer value // Cambiado de int a Integer
    ) {
        PrioritiesPaginationResponse response = prioritiesService.getAllPriorities(page, size, name, value);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Update a priority by ID")
    @ApiResponse(responseCode = "200", description = "Priority updated successfully")
    @PutMapping(value = "update/{id}")
    public ResponseEntity<?> updateById(@PathVariable Integer id, @RequestBody PrioritiesDTO updatedDTO) {
        PrioritiesDTO prioritiesDTO = prioritiesService.updateById(id, updatedDTO);
        return ResponseEntity.ok(prioritiesDTO);
    }

    @Operation(summary = "Delete a priority by ID")
    @ApiResponse(responseCode = "200", description = "Priority deleted successfully")
    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        PrioritiesDTO prioritiesDTO = prioritiesService.deleteById(id);
        return ResponseEntity.ok(prioritiesDTO);
    }
}
