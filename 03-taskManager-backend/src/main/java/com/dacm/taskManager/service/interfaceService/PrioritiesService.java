package com.dacm.taskManager.service.interfaceService;

import com.dacm.taskManager.dto.PrioritiesDTO;
import com.dacm.taskManager.entity.Priorities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface PrioritiesService {

    PrioritiesDTO convertoToDTO(Priorities priorities);
    PrioritiesDTO getByName(String name);
    PrioritiesDTO getById(Integer id);
    List<PrioritiesDTO> getAllPrioritiesDTO();
    Page<PrioritiesDTO> getAllUsersDTO(PageRequest pageRequest, String name, int value);

    PrioritiesDTO updateById(Integer id, PrioritiesDTO updatedPriority);
    PrioritiesDTO deleteById(Integer id);
    void saveManyPriorities(Priorities priorities);

}
