package com.dacm.taskManager.service.interfaceService;

import com.dacm.taskManager.dto.PrioritiesDTO;
import com.dacm.taskManager.entity.Priorities;
import com.dacm.taskManager.responses.AddedResponse;
import com.dacm.taskManager.responses.PrioritiesPaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface PrioritiesService {

    PrioritiesDTO createPriority(Priorities priorities);
    AddedResponse createManyPriorities(Priorities[] priorities);
    PrioritiesDTO convertToDTO(Priorities priorities);
    PrioritiesDTO getByName(String name);
    PrioritiesDTO getById(Integer id);
    List<PrioritiesDTO> getAllPrioritiesDTO();
    Page<PrioritiesDTO> getAllUsersDTO(PageRequest pageRequest, String name, int value);
    PrioritiesPaginationResponse getAllPriorities(int page, int size, String name, Integer value);
    Specification<Priorities> createSpecification(String name, Integer value);
    PrioritiesPaginationResponse createPaginationResponse(Page<PrioritiesDTO> prioritiesPage);
    PrioritiesDTO updateById(Integer id, PrioritiesDTO updatedPriority);
    PrioritiesDTO deleteById(Integer id);

}
