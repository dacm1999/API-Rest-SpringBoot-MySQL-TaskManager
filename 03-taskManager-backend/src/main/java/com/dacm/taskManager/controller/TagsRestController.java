package com.dacm.taskManager.controller;

import com.dacm.taskManager.entity.Tags;
import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.responses.AddedResponse;
import com.dacm.taskManager.responses.TagsErrorResponse;
import com.dacm.taskManager.repository.TagRepository;
import com.dacm.taskManager.responses.TagsPaginationResponse;
import com.dacm.taskManager.service.implementedService.TagServiceImpl;
import com.dacm.taskManager.dto.TagsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("/v1/tags")
public class TagsRestController {

    @Autowired
    private final TagServiceImpl tagService;

    @Operation(summary = "Add a single tag")
    @ApiResponse(responseCode = "200", description = "Tag added successfully")
    @PostMapping(value = "/create")
    public ResponseEntity<?> addSingleTag(@RequestBody Tags tags) {
        try {
            TagsDTO tagsDTO = tagService.createSingleTag(tags);
            return ResponseEntity.ok(tagsDTO);
        } catch (CommonErrorResponse e) {
            TagsErrorResponse errorModel = new TagsErrorResponse(tags.getName(), e.getMessage());
            List<TagsErrorResponse> failed = new ArrayList<>();
            failed.add(errorModel);
            return ResponseEntity.badRequest().body(failed);
        }
    }

    @Operation(summary = "Add multiple tags")
    @ApiResponse(responseCode = "200", description = "Tags added successfully")
    @PostMapping(value = "/createMany")
    public ResponseEntity<?> addManyTags(@RequestBody Tags[] tags) {
        AddedResponse result = tagService.createManyTags(tags);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get tag by name")
    @ApiResponse(responseCode = "200", description = "Tag found")
    @GetMapping(value = "info/name/{tagName}")
    public ResponseEntity<?> getByTagName(@PathVariable String tagName) {
        TagsDTO tagsDTO = tagService.getTag(tagName);

        if (tagsDTO == null) {
            throw new CommonErrorResponse("Tag not found: " + tagName);
        }

        return ResponseEntity.ok(tagsDTO);
    }

    @Operation(summary = "Get tag by ID")
    @ApiResponse(responseCode = "200", description = "Tag found")
    @GetMapping(value = "info/id/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        TagsDTO tagsDTO = tagService.getTagById(id);

        if (tagsDTO == null) {
            throw new CommonErrorResponse("Tag not found by ID " + id);
        }
        return ResponseEntity.ok(tagsDTO);
    }

    @Operation(summary = "Get all tags")
    @ApiResponse(responseCode = "200", description = "All tags")
    @GetMapping(value = "/allTags")
    public ResponseEntity<?> showAllTags(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description
    ) {
        Page<TagsDTO> tagsDTOPage = tagService.getAllTagsDTOPage(PageRequest.of(page, size), name, description);

        TagsPaginationResponse response = tagService.createTagsPaginationResponse(tagsDTOPage);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update tag by ID")
    @ApiResponse(responseCode = "200", description = "Tag updated successfully")
    @PutMapping(value = "update/{id}")
    public ResponseEntity<?> updateTagByID(@PathVariable Integer id, @RequestBody TagsDTO updatedTagDTO) {
        try {
            TagsDTO updatedTag = tagService.updateTagById(id, updatedTagDTO);
            return ResponseEntity.ok(updatedTag);
        } catch (NoSuchElementException e) {
            throw new CommonErrorResponse("Tag not found with ID: " + id);
        }
    }

    @Operation(summary = "Delete tag by ID")
    @ApiResponse(responseCode = "200", description = "Tag deleted successfully")
    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<TagsDTO> deleteByID(@PathVariable Integer id) {
        try {
            TagsDTO deletedByTagId = tagService.deleteById(id);
            return ResponseEntity.ok(deletedByTagId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
