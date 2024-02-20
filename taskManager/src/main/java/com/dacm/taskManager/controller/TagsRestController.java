package com.dacm.taskManager.controller;

import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.repository.TagRepository;
import com.dacm.taskManager.tags.TagServiceImpl;
import com.dacm.taskManager.tags.TagsDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
public class TagsRestController {

    @Autowired
    private final TagServiceImpl tagService;
    @Autowired
    private final TagRepository tagRepository;

    @GetMapping(value = "/{tagName}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<TagsDAO> getByTagName(@PathVariable String tagName){
        TagsDAO tagsDAO = tagService.getTag(tagName);

        if(tagsDAO == null){
            throw new CommonErrorResponse("Tag not found " + tagName);
        }

        return ResponseEntity.ok(tagsDAO);
    }

    @GetMapping(value = "/id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<TagsDAO> getById(@PathVariable Integer id){
        TagsDAO tagsDAO = tagService.getTagById(id);

        if(tagsDAO == null){
            throw new CommonErrorResponse("Tag not found by ID " + id);
        }

        return ResponseEntity.ok(tagsDAO);

    }

    @GetMapping(value = "/allTags")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TagsDAO>> showAllTags(){
        List<TagsDAO> tagsDAOList = tagService.getAllTagsDTO();
        return ResponseEntity.ok(tagsDAOList);
    }
}
