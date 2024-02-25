package com.dacm.taskManager.controller;

import com.dacm.taskManager.dto.PrioritiesDTO;
import com.dacm.taskManager.entity.Priorities;
import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.model.AddModel;
import com.dacm.taskManager.model.PrioritiesErrorModel;
import com.dacm.taskManager.model.TagsErrorModel;
import com.dacm.taskManager.repository.PrioritiesRepository;
import com.dacm.taskManager.service.implementService.PrioritiesServiceImpl;
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
@RequestMapping("/api/v1/priorities")
public class PrioritiesRestController {

    @Autowired
    private final PrioritiesServiceImpl prioritiesService;
    @Autowired
    private final PrioritiesRepository prioritiesRepository;
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
    public ResponseEntity<List<PrioritiesDTO>> getAll() {
        prioritiesDTOList = prioritiesService.getAllPrioritiesDTO();
        return ResponseEntity.ok(prioritiesDTOList);
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
        List<PrioritiesErrorModel> failed = new ArrayList<>();
        boolean repeatName = false;

        for(PrioritiesDTO tempDTO : prioritiesDTOList){
            if(priorities.getName().equals(tempDTO.getName())){
                PrioritiesErrorModel errorModel = new PrioritiesErrorModel(prioritiesDTO.getName(), "COULD NOT ADD BECAUSE PRIORITY NAME IS DUPLICATED");
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
    public ResponseEntity<AddModel> addManyPriorities(@RequestBody Priorities[] priorities) {
        prioritiesDTOList = prioritiesService.getAllPrioritiesDTO();
        AddModel result;
        String reason = "";
        int total = priorities.length;
        int count_added = 0;
        int count_failed = 0;
        List<Priorities> added = new ArrayList<>();
        List<TagsErrorModel> failed = new ArrayList<>();

        for (Priorities tempPriorities : priorities) {
            boolean repeatName = false;
            for (PrioritiesDTO dto : prioritiesDTOList) {
                if (tempPriorities.getName().equals(dto.getName())) {
                    TagsErrorModel errorModel = new TagsErrorModel(tempPriorities.getName(), "DUPLICATED VALUE");
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
        result = new AddModel(true, total, count_added, count_failed, (ArrayList<Priorities>) added, (ArrayList<TagsErrorModel>) failed, reason);
        return ResponseEntity.ok(result);
    }



}
