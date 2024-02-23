package com.dacm.taskManager.controller;

import com.dacm.taskManager.entity.Tags;
import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.model.AddModel;
import com.dacm.taskManager.model.TagsErrorModel;
import com.dacm.taskManager.repository.TagRepository;
import com.dacm.taskManager.service.impl.TagServiceImpl;
import com.dacm.taskManager.dto.TagsDTO;
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
@RequestMapping("/api/v1/tags")
public class TagsRestController {

    @Autowired
    private final TagServiceImpl tagService;
    @Autowired
    private final TagRepository tagsRepository;

    @GetMapping(value = "/{tagName}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<TagsDTO> getByTagName(@PathVariable String tagName) {
        TagsDTO tagsDAO = tagService.getTag(tagName);

        if (tagsDAO == null) {
            throw new CommonErrorResponse("Tag not found " + tagName);
        }

        return ResponseEntity.ok(tagsDAO);
    }

    @GetMapping(value = "/id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<TagsDTO> getById(@PathVariable Integer id) {
        TagsDTO tagsDAO = tagService.getTagById(id);

        if (tagsDAO == null) {
            throw new CommonErrorResponse("Tag not found by ID " + id);
        }

        return ResponseEntity.ok(tagsDAO);

    }

    @GetMapping(value = "/allTags")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TagsDTO>> showAllTags() {
        List<TagsDTO> tagsDAOList = tagService.getAllTagsDTO();
        return ResponseEntity.ok(tagsDAOList);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<TagsDTO> updateTagByID(@PathVariable Integer id, @RequestBody TagsDTO updatedTagDTO) {
        try {
            TagsDTO updatedTag = tagService.updateTagById(id, updatedTagDTO);
            return ResponseEntity.ok(updatedTag);
        } catch (NoSuchElementException e) {
            // Manejar el caso en que el usuario no se encuentre
            throw new CommonErrorResponse("Tag not found with ID: " + id);
        } catch (Exception e) {
            // Manejar otros posibles errores
            throw new CommonErrorResponse("Error updating user with ID: " + id, e);
        }

    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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

    @PostMapping(value = "/single")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> addSingleTag(@RequestBody Tags tags) {
        TagsDTO tagsDTO = null;

        // Get all existing tags DTOs and initialize a list to store failed operations
        List<TagsDTO> tagsDTOList = tagService.getAllTagsDTO();
        List<TagsErrorModel> failed = new ArrayList<>();

        boolean repeatCode = false; // Move this declaration out of the for loop

        // Iterate through existing tags DTOs to check for duplicates
        for (TagsDTO tempTagDTO : tagsDTOList) {
            // Check if the tag name already exists
            if (tags.getName().equals(tempTagDTO.getName())) {
                // If duplicate found, create an error model and add it to the list of failed operations
                TagsErrorModel errorModel = new TagsErrorModel(tags.getName(), "COULD NOT ADD BECAUSE TAG NAME IS DUPLICATED");
                repeatCode = true;
                failed.add(errorModel);
                return ResponseEntity.ok(failed); // Return the list of failed operations as response
//                return ResponseEntity.badRequest(failed); // Return the list of failed operations as response
            }
        }

        // If no duplicate found, proceed to save the new tag
        if (!repeatCode) {
            // Save the Tags object using the repository
            Tags savedTags = tagsRepository.save(tags);
            // Convert the saved tag to DTO
            tagsDTO = tagService.convertToDTO(savedTags); // Assuming you have a method to convert Tags to TagsDTO
        }

        // Return the DTO of the saved tag or null if no duplicate was found
        return ResponseEntity.ok(tagsDTO);
    }

    @PostMapping(value = "/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AddModel> addManyTags(@RequestBody Tags[] tags) {
        List<TagsDTO> tagsDTOList = tagService.getAllTagsDTO();
        AddModel result;
        String reason = "";
        int total = tags.length;
        int count_added = 0;
        int count_failed = 0;
        List<TagsErrorModel> failed = new ArrayList<>();
        List<Tags> addedTags = new ArrayList<>();

        for (Tags tag : tags) {
            boolean repeatCode = false;
            for (TagsDTO tagsDTO : tagsDTOList) {
                if (tag.getName().equals(tagsDTO.getName())) {
                    TagsErrorModel errorModel = new TagsErrorModel(tag.getName(), "TAG NAME REPEATED");
                    failed.add(errorModel);
                    count_failed++;
                    repeatCode = true;
                    reason = "COULD NOT ADD THIS TAGS";
                    break;
                }
            }
            if (!repeatCode) {
                int id = tagService.saveManyTags(tag);
                if (id != -1) {
                    reason = "Tags added successfully";
                    addedTags.add(tag);
                    count_added++;
                }
            }
        }

        result = new AddModel(true, total, count_added, count_failed, (ArrayList) addedTags, (ArrayList) failed, reason);
        return ResponseEntity.ok(result);
    }

}
