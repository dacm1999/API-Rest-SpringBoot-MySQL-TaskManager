package com.dacm.taskManager.controller;

import com.dacm.taskManager.dto.PrioritiesDTO;
import com.dacm.taskManager.entity.Priorities;
import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.responses.AddedResponse;
import com.dacm.taskManager.responses.PrioritiesErrorResponse;
import com.dacm.taskManager.responses.TagsErrorResponse;
import com.dacm.taskManager.repository.PriorityRepository;
import com.dacm.taskManager.responses.PrioritiesPaginationResponse;
import com.dacm.taskManager.service.implementService.PrioritiesServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/priorities")
public class PrioritiesRestController {

    @Autowired
    private final PrioritiesServiceImpl prioritiesService;
    @Autowired
    private final PriorityRepository prioritiesRepository;
    private PrioritiesDTO prioritiesDTO;
    private List<PrioritiesDTO> prioritiesDTOList;

    @GetMapping(value = "/{name}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<PrioritiesDTO> getByName(@PathVariable String name) {
        prioritiesDTO = prioritiesService.getByName(name);

        if (prioritiesDTO == null) {
            throw new CommonErrorResponse("COULD NOT FOUND " + name);
        }

        return ResponseEntity.ok(prioritiesDTO);
    }

    @GetMapping(value = "/id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<PrioritiesDTO> getById(@PathVariable Integer id) {

        prioritiesDTO = prioritiesService.getById(id);
        if (prioritiesDTO == null) {
            throw new CommonErrorResponse("Priority could not found by ID " + id);
        }

        return ResponseEntity.ok(prioritiesDTO);
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<PrioritiesPaginationResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) int value
    ) {
        Page<PrioritiesDTO> prioritiesPage = prioritiesService.getAllUsersDTO(
                PageRequest.of(page,size),
                name,
                value
        );

        PrioritiesPaginationResponse response = new PrioritiesPaginationResponse();
        response.setPriorities(prioritiesPage.getContent());
        response.setTotalPages(prioritiesPage.getTotalPages());
        response.setTotalElements(prioritiesPage.getTotalElements());
        response.setNumberOfElements(prioritiesPage.getNumberOfElements());
        response.setSize(prioritiesPage.getSize());

        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<PrioritiesDTO> updateById(@PathVariable Integer id, @RequestBody PrioritiesDTO updatedDTO) {
        try {
            prioritiesDTO = prioritiesService.updateById(id, updatedDTO);
            return ResponseEntity.ok(prioritiesDTO);
        } catch (NoSuchElementException e) {
            throw new CommonErrorResponse("User not found with ID: " + id);
        }
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PrioritiesDTO> deleteById(@PathVariable Integer id) {
        try {
            prioritiesDTO = prioritiesService.deleteById(id);
            return ResponseEntity.ok(prioritiesDTO);
        } catch (NoSuchElementException e) {
            throw new CommonErrorResponse("Could not found with id " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/single")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> addSingle(@RequestBody Priorities priorities) {

        prioritiesDTOList = prioritiesService.getAllPrioritiesDTO();
        List<PrioritiesErrorResponse> failed = new ArrayList<>();
        boolean repeatName = false;

        for(PrioritiesDTO tempDTO : prioritiesDTOList){
            if(priorities.getName().equals(tempDTO.getName())){
                PrioritiesErrorResponse errorModel = new PrioritiesErrorResponse(prioritiesDTO.getName(), "COULD NOT ADD BECAUSE PRIORITY NAME IS DUPLICATED");
                repeatName = true;
                failed.add(errorModel);

                return ResponseEntity.badRequest().body(errorModel);
            }

            if(!repeatName){
                Priorities priorities1 = prioritiesRepository.save(priorities);
                prioritiesDTO = prioritiesService.convertoToDTO(priorities1);
            }
        }

        return ResponseEntity.ok(prioritiesDTO);
    }

    @PostMapping(value = "/")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<AddedResponse> addManyPriorities(@RequestBody Priorities[] priorities) {
        prioritiesDTOList = prioritiesService.getAllPrioritiesDTO();
        AddedResponse result;
        String reason = "";
        int total = priorities.length;
        int count_added = 0;
        int count_failed = 0;
        List<Priorities> added = new ArrayList<>();
        List<TagsErrorResponse> failed = new ArrayList<>();

        for (Priorities tempPriorities : priorities) {
            boolean repeatName = false;
            for (PrioritiesDTO dto : prioritiesDTOList) {
                if (tempPriorities.getName().equals(dto.getName())) {
                    TagsErrorResponse errorModel = new TagsErrorResponse(tempPriorities.getName(), "DUPLICATED VALUE");
                    failed.add(errorModel);
                    count_failed++;
                    repeatName = true;
                    reason = "COULD NOT ADD THIS NAMES";
                    break; // Break out of the inner loop if duplicate name is found
                }
            }

            if (!repeatName) {
                prioritiesService.saveManyPriorities(tempPriorities);
                reason = "Names added successfully";
                added.add(tempPriorities);
                count_added++;
            }
        }
        result = new AddedResponse(true, total, count_added, count_failed, (ArrayList<Priorities>) added, (ArrayList<TagsErrorResponse>) failed, reason);
        return ResponseEntity.ok(result);
    }



}
