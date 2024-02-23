package com.dacm.taskManager.service;

import com.dacm.taskManager.dto.PrioritiesDTO;
import com.dacm.taskManager.entity.Priorities;

import java.util.List;

public interface PrioritiesService {

    PrioritiesDTO convertoToDTO(Priorities priorities);
    PrioritiesDTO getByName(String name);
    PrioritiesDTO getById(Integer id);
    List<PrioritiesDTO> getAllPrioritiesDTO();
    PrioritiesDTO updateById(Integer id, PrioritiesDTO updatedPriority);
    PrioritiesDTO deleteById(Integer id);
    void saveManyPriorities(Priorities priorities);

}
