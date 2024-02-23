package com.dacm.taskManager.service.impl;

import com.dacm.taskManager.dto.PrioritiesDTO;
import com.dacm.taskManager.entity.Priorities;
import com.dacm.taskManager.repository.PrioritiesRepository;
import com.dacm.taskManager.service.PrioritiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PrioritiesServiceImpl implements PrioritiesService {

    private final PrioritiesRepository prioritiesRepository;
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
