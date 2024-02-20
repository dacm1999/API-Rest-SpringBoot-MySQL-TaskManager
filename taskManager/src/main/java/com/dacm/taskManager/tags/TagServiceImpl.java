package com.dacm.taskManager.tags;

import com.dacm.taskManager.entity.Tags;
import com.dacm.taskManager.repository.TagRepository;
import com.dacm.taskManager.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;


    @Override
    public TagsDAO convertToDTO(Tags tags) {
        TagsDAO tagsDAO = new TagsDAO();
        tagsDAO.setName(tags.getName());
        tagsDAO.setDescription(tags.getDescription());
        return tagsDAO;
    }

    @Override
    public TagsDAO getTag(String tagName) {
        // Get the Tags object from the database
        Tags tags = tagRepository.findByName(tagName);

        // Check if the tag is found in the database
        if (tags != null) {

            TagsDAO tagsDAO = new TagsDAO(); // Create an instance of TagsDAO
            tagsDAO.setName(tags.getName()); // Assuming there is a setName method in TagsDAO to set the name
            tagsDAO.setDescription(tags.getDescription()); // Assuming there is a setName method in TagsDAO to set the name

            return tagsDAO; // Return the TagsDAO object
        }

        return null; // Return null if the tag is not found in the database
    }

    @Override
    public TagsDAO getTagById(Integer id) {
        Tags tags = tagRepository.findById(id).orElseThrow(null);

        if(tags != null){
            TagsDAO tagsDAO = new TagsDAO();
            tagsDAO.setName(tags.getName());
            tagsDAO.setDescription(tags.getDescription());
            return tagsDAO;
        }

        return null;
    }

    @Override
    public TagsDAO updateTagById(Integer id) {
        return null;
    }

    @Override
    public TagsDAO deleteById(Integer id) {
        return null;
    }

    @Override
    public List<TagsDAO> getAllTagsDTO() {
        List<Tags> tagsList = tagRepository.findAll();
        List<TagsDAO> tagsDAOList = new ArrayList<>();

        for(Tags tempTags : tagsList){
            TagsDAO tagsDAO = convertToDTO(tempTags);
            tagsDAOList.add(tagsDAO);
        }
        return  tagsDAOList;
    }
}
