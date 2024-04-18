package com.dacm.taskManager.service.implementedService;

import com.dacm.taskManager.dto.PrioritiesDTO;
import com.dacm.taskManager.entity.Priorities;
import com.dacm.taskManager.repository.PriorityRepository;
import com.dacm.taskManager.service.interfaceService.PrioritiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PrioritiesServiceImpl implements PrioritiesService {

    private final PriorityRepository prioritiesRepository;
    @Override
    public PrioritiesDTO convertoToDTO(Priorities priorities) {
        PrioritiesDTO prioritiesDTO = new PrioritiesDTO();
        prioritiesDTO.setName(priorities.getName());
        prioritiesDTO.setValue(priorities.getValue());
        return prioritiesDTO;
    }

    @Override
    public PrioritiesDTO getByName(String name) {
        Priorities priorities = prioritiesRepository.findByName(name);

        if(priorities != null){
            PrioritiesDTO prioritiesDTO = new PrioritiesDTO();
            prioritiesDTO.setName(priorities.getName());
            prioritiesDTO.setValue(priorities.getValue());
            return prioritiesDTO;
        }

        return null;
    }

    @Override
    public PrioritiesDTO getById(Integer id) {
        Priorities priorities = prioritiesRepository.findById(id).orElseThrow(null);

        if(priorities != null){
            PrioritiesDTO prioritiesDTO = new PrioritiesDTO();
            prioritiesDTO.setName(priorities.getName());
            prioritiesDTO.setValue(priorities.getValue());
            return  prioritiesDTO;
        }
        return null;
    }

    @Override
    public List<PrioritiesDTO> getAllPrioritiesDTO() {
        List<Priorities> prioritiesList = prioritiesRepository.findAll();
        List<PrioritiesDTO> prioritiesDTOS = new ArrayList<>();

        for(Priorities tempPri : prioritiesList){
            PrioritiesDTO prioritiesDTO = convertoToDTO(tempPri);
            prioritiesDTOS.add(prioritiesDTO);
        }

        return prioritiesDTOS;
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

        Page<Priorities> prioritiesPage = prioritiesRepository.findAll(specification,pageRequest);

        return prioritiesPage.map(this::convertoToDTO);
    }

    @Override
    public PrioritiesDTO updateById(Integer id, PrioritiesDTO updatedPriority) {
        Priorities priorities = prioritiesRepository.findById(id).orElseThrow();

        priorities.setName(updatedPriority.getName());
        priorities.setValue(updatedPriority.getValue());

        Priorities updated = prioritiesRepository.save(priorities);

        return convertoToDTO(updated);
    }

    @Override
    public PrioritiesDTO deleteById(Integer id) {
        Priorities priorities = prioritiesRepository.findById(id).orElseThrow(()-> new NoSuchElementException("Tag not found with id: " + id));
        prioritiesRepository.deleteById(id);
        return convertoToDTO(priorities);
    }

    @Override
    public void saveManyPriorities(Priorities priorities) {
        Priorities existingPriorities = prioritiesRepository.findByName(priorities.getName());

        if(existingPriorities == null){
            prioritiesRepository.save(priorities);
        }

    }
}
