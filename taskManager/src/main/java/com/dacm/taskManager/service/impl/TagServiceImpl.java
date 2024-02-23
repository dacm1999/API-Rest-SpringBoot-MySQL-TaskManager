package com.dacm.taskManager.service.impl;

import com.dacm.taskManager.dto.TagsDTO;
import com.dacm.taskManager.entity.Tags;
import com.dacm.taskManager.repository.TagRepository;
import com.dacm.taskManager.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;


    @Override
    public TagsDTO convertToDTO(Tags tags) {
        TagsDTO tagsDTO = new TagsDTO();
        tagsDTO.setName(tags.getName());
        tagsDTO.setDescription(tags.getDescription());
        return tagsDTO;
    }

    @Override
    public TagsDTO getTag(String tagName) {
        // Get the Tags object from the database
        Tags tags = tagRepository.findByName(tagName);

        // Check if the tag is found in the database
        if (tags != null) {

            TagsDTO tagsDAO = new TagsDTO(); // Create an instance of TagsDAO
            tagsDAO.setName(tags.getName()); // Assuming there is a setName method in TagsDAO to set the name
            tagsDAO.setDescription(tags.getDescription()); // Assuming there is a setName method in TagsDAO to set the name

            return tagsDAO; // Return the TagsDAO object
        }

        return null; // Return null if the tag is not found in the database
    }

    @Override
    public TagsDTO getTagById(Integer id) {
        Tags tags = tagRepository.findById(id).orElseThrow(null);

        if(tags != null){
            TagsDTO tagsDAO = new TagsDTO();
            tagsDAO.setName(tags.getName());
            tagsDAO.setDescription(tags.getDescription());
            return tagsDAO;
        }

        return null;
    }

    @Override
    public TagsDTO updateTagById(Integer id, TagsDTO updatedTags) {

        Tags tags = tagRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Tag not found with id: " + id));

        // Update user fields with new values
        tags.setName(updatedTags.getName());
        tags.setDescription(updatedTags.getDescription());

        // Save all changes at repository
        Tags updatedTag = tagRepository.save(tags);

        // Convert all updated users a DTO and return them
        return convertToDTO(updatedTag);
    }

    @Override
    public TagsDTO deleteById(Integer id) {
        Tags tags = tagRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Tag not found with id: " + id));
        tagRepository.deleteById(id);
        return convertToDTO(tags);
    }

    @Override
    public List<TagsDTO> getAllTagsDTO() {
        List<Tags> tagsList = tagRepository.findAll();
        List<TagsDTO> tagsDAOList = new ArrayList<>();

        for(Tags tempTags : tagsList){
            TagsDTO tagsDAO = convertToDTO(tempTags);
            tagsDAOList.add(tagsDAO);
        }
        return  tagsDAOList;
    }

    @Override
    public int saveManyTags(Tags tags) {
        int numberOfTagsSaved = 0;
        Tags existingTag = tagRepository.findByName(tags.getName());
        if (existingTag == null) {
            tagRepository.save(tags);
            numberOfTagsSaved = 1;
        }
        return numberOfTagsSaved;
    }
}
