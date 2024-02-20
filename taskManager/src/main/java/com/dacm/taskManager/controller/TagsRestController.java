package com.dacm.taskManager.controller;

import com.dacm.taskManager.entity.Tags;
import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.model.AddModel;
import com.dacm.taskManager.repository.TagRepository;
import com.dacm.taskManager.tags.TagServiceImpl;
import com.dacm.taskManager.tags.TagsDTO;
import com.dacm.taskManager.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<TagsDTO> getByTagName(@PathVariable String tagName){
        TagsDTO tagsDAO = tagService.getTag(tagName);

        if(tagsDAO == null){
            throw new CommonErrorResponse("Tag not found " + tagName);
        }

        return ResponseEntity.ok(tagsDAO);
    }

    @GetMapping(value = "/id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<TagsDTO> getById(@PathVariable Integer id){
        TagsDTO tagsDAO = tagService.getTagById(id);

        if(tagsDAO == null){
            throw new CommonErrorResponse("Tag not found by ID " + id);
        }

        return ResponseEntity.ok(tagsDAO);

    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagsDTO> deleteByID(@PathVariable Integer id){
        try{
            TagsDTO deletedByTagId = tagService.deleteById(id);
            return ResponseEntity.ok(deletedByTagId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<TagsDTO> updateTagByID(@PathVariable Integer id, @RequestBody TagsDTO updatedTagDTO){
        try {
            TagsDTO updatedTag = tagService.updateTagById(id,updatedTagDTO);
            return ResponseEntity.ok(updatedTag);
        } catch (NoSuchElementException e) {
            // Manejar el caso en que el usuario no se encuentre
            throw new CommonErrorResponse("User not found with ID: " + id);
        } catch (Exception e) {
            // Manejar otros posibles errores
            throw new CommonErrorResponse("Error updating user with ID: " + id, e);
        }

    }

    @PostMapping(value = "/")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<AddModel> addManyTags(@RequestBody Tags [] tags){
        AddModel result = null;

        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/single")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<TagsDTO> addSingleTag(@RequestBody Tags tags){
        TagsDTO tagsDTO = null;

        Tags savedTags = tagsRepository.save(tags); // Saving the Tags object using the repository

        tagsDTO = tagService.convertToDTO(savedTags); // Assuming you have a method to convert Tags to TagsDTO

        return ResponseEntity.ok(tagsDTO);
    }



    @GetMapping(value = "/allTags")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TagsDTO>> showAllTags(){
        List<TagsDTO> tagsDAOList = tagService.getAllTagsDTO();
        return ResponseEntity.ok(tagsDAOList);
    }
}
