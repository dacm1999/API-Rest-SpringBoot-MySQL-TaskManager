package com.dacm.taskManager.responses;

import com.dacm.taskManager.dto.UserDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class UserPaginationResponse {

    private List<UserDTO> users;
    private long totalElements;
    private int totalPages;
    private int numberOfElements;
    private int size;
    private int number;

}
