package com.dacm.taskManager.service;

import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.dto.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    UserDTO convertToDTO(User user);
    UserDTO getUser(String username);
    UserDTO getUser(Integer id);
    UserDTO updateUserById(Integer id, UserDTO updatedUserDTO);
    UserDTO deleteUserById(Integer id);
    List<UserDTO> getAllUsersDTO();
    List<String> getAllUsernames();
    List<String> getAllEmails();
    int save(User users);
}
