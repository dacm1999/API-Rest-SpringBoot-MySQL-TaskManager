package com.dacm.taskManager.service.interfaceService;

import com.dacm.taskManager.entity.Tags;
import com.dacm.taskManager.dto.TagsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface TagService {

    TagsDTO convertToDTO(Tags tags);
    TagsDTO getTag(String tagName);
    TagsDTO getTagById(Integer id);
    TagsDTO updateTagById(Integer id, TagsDTO updatedTagDao);
    TagsDTO deleteById(Integer id);
    List<TagsDTO> getAllTagsDTO();
    Page<TagsDTO> getAllTagsDTOPage(PageRequest pageRequest, String name, String description);
    int saveManyTags(Tags tags);

}
