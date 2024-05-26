package com.dacm.taskManager.service.implementedService;

import com.dacm.taskManager.dto.TagsDTO;
import com.dacm.taskManager.entity.Tags;
import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.repository.TagRepository;
import com.dacm.taskManager.responses.AddedResponse;
import com.dacm.taskManager.responses.TagsErrorResponse;
import com.dacm.taskManager.responses.TagsPaginationResponse;
import com.dacm.taskManager.service.interfaceService.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public TagsDTO createSingleTag(Tags tags) {
        List<Tags> existingTags = tagRepository.findAll();

        for (Tags existingTag : existingTags) {
            if (tags.getName().equals(existingTag.getName())) {
                throw new CommonErrorResponse("COULD NOT ADD BECAUSE TAG NAME IS DUPLICATED");
            }
        }

        Tags savedTags = tagRepository.save(tags);
        return convertToDTO(savedTags);
    }

    @Override
    public AddedResponse createManyTags(Tags[] tags) {
        List<Tags> existingTags = tagRepository.findAll();
        List<TagsDTO> tagsDTOList = convertToDTOList(existingTags);

        int total = tags.length;
        int countAdded = 0;
        int countFailed = 0;
        List<TagsErrorResponse> failed = new ArrayList<>();
        List<Tags> addedTags = new ArrayList<>();

        for (Tags tag : tags) {
            boolean isDuplicate = false;
            for (TagsDTO tagsDTO : tagsDTOList) {
                if (tag.getName().equals(tagsDTO.getName())) {
                    TagsErrorResponse errorModel = new TagsErrorResponse(
                            tag.getName(),
                            "TAG NAME REPEATED"
                    );
                    failed.add(errorModel);
                    countFailed++;
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) {
                Tags savedTags = tagRepository.save(tag);
                addedTags.add(savedTags);
                countAdded++;
            }
        }

        String reason = countFailed > 0 ? "Some tags could not be added due to duplication" : "Tags added successfully";
        return new AddedResponse(true, total, countAdded, countFailed, (ArrayList<Tags>) addedTags, (ArrayList<TagsErrorResponse>) failed, reason);
    }

    @Override
    public TagsDTO convertToDTO(Tags tags) {
        TagsDTO tagsDTO = new TagsDTO();
        tagsDTO.setName(tags.getName());
        tagsDTO.setDescription(tags.getDescription());
        return tagsDTO;
    }

    @Override
    public TagsDTO getTag(String tagName) {
        Tags tags = tagRepository.findByName(tagName);

        if (tags != null) {
            return convertToDTO(tags);
        }
        return null;
    }

    @Override
    public TagsDTO getTagById(Integer id) {
        Tags tags = tagRepository.findById(id)
                .orElseThrow(() -> new CommonErrorResponse("Tag not found by ID " + id));

        TagsDTO tagsDTO = new TagsDTO();
        tagsDTO.setName(tags.getName());
        tagsDTO.setDescription(tags.getDescription());
        return tagsDTO;
    }

    @Override
    public TagsDTO updateTagById(Integer id, TagsDTO updatedTags) {
        Tags tags = tagRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tag not found with id: " + id));

        tags.setName(updatedTags.getName());
        tags.setDescription(updatedTags.getDescription());

        Tags updatedTag = tagRepository.save(tags);

        return convertToDTO(updatedTag);
    }

    @Override
    public TagsDTO deleteById(Integer id) {
        Tags tags = tagRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tag not found with id: " + id));
        tagRepository.deleteById(id);
        return convertToDTO(tags);
    }

    @Override
    public List<TagsDTO> getAllTagsDTO() {
        List<Tags> tagsList = tagRepository.findAll();
        List<TagsDTO> tagsDAOList = new ArrayList<>();

        for (Tags tempTags : tagsList) {
            TagsDTO tagsDAO = convertToDTO(tempTags);
            tagsDAOList.add(tagsDAO);
        }
        return tagsDAOList;
    }

    @Override
    public Page<TagsDTO> getAllTagsDTOPage(PageRequest pageRequest, String name, String description) {
        Specification<Tags> specification = Specification.where(null);

        if (name != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name));
        }
        if (description != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("description"), description));
        }

        return tagRepository.findAll(specification, pageRequest).map(this::convertToDTO);
    }

    @Override
    public TagsPaginationResponse createTagsPaginationResponse(Page<TagsDTO> tagsDTOPage) {
        TagsPaginationResponse response = new TagsPaginationResponse();
        response.setTags(tagsDTOPage.getContent());
        response.setTotalElements(tagsDTOPage.getTotalElements());
        response.setTotalPages(tagsDTOPage.getTotalPages());
        response.setNumberOfElements(tagsDTOPage.getNumberOfElements());
        response.setSize(tagsDTOPage.getSize());
        return response;
    }

    public List<TagsDTO> convertToDTOList(List<Tags> tagsList) {
        List<TagsDTO> tagsDTOList = new ArrayList<>();
        for (Tags tag : tagsList) {
            tagsDTOList.add(convertToDTO(tag));
        }
        return tagsDTOList;
    }
}
