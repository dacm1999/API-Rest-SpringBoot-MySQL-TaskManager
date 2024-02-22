package com.dacm.taskManager.service;

import com.dacm.taskManager.entity.Tags;
import com.dacm.taskManager.dto.TagsDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TagService {

    TagsDTO convertToDTO(Tags tags);
    TagsDTO getTag(String tagName);
    TagsDTO getTagById(Integer id);
    TagsDTO updateTagById(Integer id, TagsDTO updatedTagDao);
    TagsDTO deleteById(Integer id);
    List<TagsDTO> getAllTagsDTO();
    int saveManyTags(Tags tags);

}
