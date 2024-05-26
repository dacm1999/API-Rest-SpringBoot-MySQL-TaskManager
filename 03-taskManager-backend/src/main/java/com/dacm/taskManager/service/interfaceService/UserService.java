package com.dacm.taskManager.service.interfaceService;

import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.dto.UserDTO;
import com.dacm.taskManager.responses.AddedResponse;
import com.dacm.taskManager.responses.UserPaginationResponse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface UserService {

    AddedResponse addMultipleUsers(User [] users);
    UserDTO convertToDTO(User user);
    UserDTO getUser(String username);
    UserDTO getUser(Integer id);
    UserDTO updateUserById(Integer id, UserDTO updatedUserDTO);
    UserDTO deleteUserById(Integer id);
    UserPaginationResponse getAllUsersDTO(PageRequest pageRequest, String username, String firstname, String lastname, String email);
    List<String> getAllUsernames();
    List<String> getAllEmails();
    int save(User users);
}
