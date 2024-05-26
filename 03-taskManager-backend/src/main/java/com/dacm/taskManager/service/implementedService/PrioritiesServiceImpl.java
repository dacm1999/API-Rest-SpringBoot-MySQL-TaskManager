package com.dacm.taskManager.service.implementedService;

import com.dacm.taskManager.dto.PrioritiesDTO;
import com.dacm.taskManager.entity.Priorities;
import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.repository.PriorityRepository;
import com.dacm.taskManager.responses.AddedResponse;
import com.dacm.taskManager.responses.PrioritiesErrorResponse;
import com.dacm.taskManager.responses.PrioritiesPaginationResponse;
import com.dacm.taskManager.responses.TagsErrorResponse;
import com.dacm.taskManager.service.interfaceService.PrioritiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PrioritiesServiceImpl implements PrioritiesService {

    private final PriorityRepository prioritiesRepository;

    @Override
    public PrioritiesDTO createPriority(Priorities priorities) {
        PrioritiesDTO prioritiesDTO = new PrioritiesDTO();
        List<PrioritiesDTO> prioritiesDTOList = getAllPrioritiesDTO();
        List<PrioritiesErrorResponse> failed = new ArrayList<>();
        boolean repeatName = false;

        for (PrioritiesDTO tempDTO : prioritiesDTOList) {
            if (priorities.getName().equals(tempDTO.getName())) {
                PrioritiesErrorResponse errorModel = new PrioritiesErrorResponse(prioritiesDTO.getName(), "COULD NOT ADD BECAUSE PRIORITY NAME IS DUPLICATED");
                repeatName = true;
                failed.add(errorModel);
                throw new CommonErrorResponse("COULD NOT ADD BECAUSE PRIORITY NAME IS DUPLICATED");
            }

            if (!repeatName) {
                priorities = prioritiesRepository.save(priorities);
                prioritiesDTO = convertToDTO(priorities);
            }

        }
        return convertToDTO(priorities);
    }

    @Override
    public AddedResponse createManyPriorities(Priorities[] priorities) {
        List<PrioritiesDTO> prioritiesDTOList = getAllPrioritiesDTO();
        String reason = "";
        int total = priorities.length;
        int countAdded = 0;
        int countFailed = 0;
        List<Priorities> added = new ArrayList<>();
        List<TagsErrorResponse> failed = new ArrayList<>();

        for (Priorities tempPriorities : priorities) {
            boolean repeatName = prioritiesDTOList.stream()
                    .anyMatch(dto -> dto.getName().equals(tempPriorities.getName()));

            if (repeatName) {
                TagsErrorResponse errorModel = new TagsErrorResponse(tempPriorities.getName(), "DUPLICATED VALUE");
                failed.add(errorModel);
                countFailed++;
                reason = "COULD NOT ADD THESE NAMES";
            } else {
                Priorities existingPriorities = prioritiesRepository.findByName(tempPriorities.getName());
                if (existingPriorities == null) {
                    prioritiesRepository.save(tempPriorities);
                    added.add(tempPriorities);
                    countAdded++;
                }
            }
        }

        reason = countFailed > 0 ? "COULD NOT ADD THESE NAMES" : "Names added successfully";
        return new AddedResponse(true, total, countAdded, countFailed, (ArrayList<Priorities>) added, (ArrayList<TagsErrorResponse>) failed, reason);
    }

    @Override
    public PrioritiesDTO convertToDTO(Priorities priorities) {
        PrioritiesDTO prioritiesDTO = new PrioritiesDTO();
        prioritiesDTO.setName(priorities.getName());
        prioritiesDTO.setValue(priorities.getValue());
        return prioritiesDTO;
    }

    @Override
    public PrioritiesDTO getByName(String name) {
        Priorities priorities = prioritiesRepository.findByName(name);

        if (priorities == null) {
            throw new CommonErrorResponse("COULD NOT FOUND " + name);
        }

        PrioritiesDTO prioritiesDTO = new PrioritiesDTO();
        prioritiesDTO.setName(priorities.getName());
        prioritiesDTO.setValue(priorities.getValue());
        return prioritiesDTO;
    }

    @Override
    public PrioritiesDTO getById(Integer id) {
        Priorities priorities = prioritiesRepository.findById(id)
                .orElseThrow(() -> new CommonErrorResponse("Priority could not found by ID " + id));

        PrioritiesDTO prioritiesDTO = new PrioritiesDTO();
        prioritiesDTO.setName(priorities.getName());
        prioritiesDTO.setValue(priorities.getValue());
        return prioritiesDTO;
    }

    @Override
    public List<PrioritiesDTO> getAllPrioritiesDTO() {
        List<Priorities> prioritiesList = prioritiesRepository.findAll();
        List<PrioritiesDTO> prioritiesDTOS = new ArrayList<>();

        for (Priorities tempPri : prioritiesList) {
            PrioritiesDTO prioritiesDTO = convertToDTO(tempPri);
            prioritiesDTOS.add(prioritiesDTO);
        }

        return prioritiesDTOS;
    }

    @Override
    public PrioritiesPaginationResponse getAllPriorities(int page, int size, String name, Integer value) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Specification<Priorities> specification = createSpecification(name, value);

        Page<Priorities> prioritiesPage = prioritiesRepository.findAll(specification, pageRequest);
        Page<PrioritiesDTO> prioritiesDTOPage = prioritiesPage.map(this::convertToDTO);

        return createPaginationResponse(prioritiesDTOPage);
    }

    @Override
    public Specification<Priorities> createSpecification(String name, Integer value) {
        Specification<Priorities> specification = Specification.where(null);
        if (name != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("name"), name)
            );
        }
        if (value != null && value != 0) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("value"), value)
            );
        }
        return specification;
    }

    @Override
    public PrioritiesPaginationResponse createPaginationResponse(Page<PrioritiesDTO> prioritiesPage) {
        PrioritiesPaginationResponse response = new PrioritiesPaginationResponse();
        response.setPriorities(prioritiesPage.getContent());
        response.setTotalPages(prioritiesPage.getTotalPages());
        response.setTotalElements(prioritiesPage.getTotalElements());
        response.setNumberOfElements(prioritiesPage.getNumberOfElements());
        response.setSize(prioritiesPage.getSize());
        return response;
    }

    @Override
    public Page<PrioritiesDTO> getAllUsersDTO(PageRequest pageRequest, String name, int value) {
        Specification<Priorities> specification = Specification.where(null);
        if (name != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name));
        }
        if (value != 0) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("value"), value));
        }

        Page<Priorities> prioritiesPage = prioritiesRepository.findAll(specification, pageRequest);

        return prioritiesPage.map(this::convertToDTO);
    }

    @Override
    public PrioritiesDTO updateById(Integer id, PrioritiesDTO updatedPriority) {
        Priorities priorities = prioritiesRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));

        priorities.setName(updatedPriority.getName());
        priorities.setValue(updatedPriority.getValue());

        Priorities updated = prioritiesRepository.save(priorities);

        return convertToDTO(updated);
    }

    @Override
    public PrioritiesDTO deleteById(Integer id) {
        Priorities priorities = prioritiesRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tag not found with id: " + id));

        prioritiesRepository.deleteById(id);

        return convertToDTO(priorities);
    }
}
