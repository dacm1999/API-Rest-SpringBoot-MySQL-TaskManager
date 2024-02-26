package com.dacm.taskManager.responses;

import com.dacm.taskManager.dto.PrioritiesDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class PrioritiesPaginationResponse {

    private List<PrioritiesDTO> priorities;
    private long totalElements;
    private int totalPages;
    private int numberOfElements;
    private int size;

}
