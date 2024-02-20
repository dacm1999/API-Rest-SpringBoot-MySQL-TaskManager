package com.dacm.taskManager.service;

import com.dacm.taskManager.entity.Tags;
import com.dacm.taskManager.tags.TagsDAO;

import java.util.List;

public interface TagService {

    TagsDAO convertToDTO(Tags tags);
    TagsDAO getTag(String tagName);
    TagsDAO getTagById(Integer id);
    TagsDAO updateTagById(Integer id);
    TagsDAO deleteById(Integer id);
    List<TagsDAO> getAllTagsDTO();

}
