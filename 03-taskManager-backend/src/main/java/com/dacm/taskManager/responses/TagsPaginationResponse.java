package com.dacm.taskManager.responses;

import com.dacm.taskManager.dto.TagsDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@RequiredArgsConstructor
public class TagsPaginationResponse {

    private List<TagsDTO> tags;
    private long totalElements;
    private int totalPages;
    private int numberOfElements;
    private int size;

}
