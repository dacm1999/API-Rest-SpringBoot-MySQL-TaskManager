package com.dacm.taskManager.service;

import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    UserDTO convertToDTO(User user);
    UserDTO getUser(String username);
    UserDTO getUser(Integer id);
    UserDTO updateUserById(Integer id, UserDTO updatedUserDTO);
    UserDTO deleteUserById(Integer id);
    Page<UserDTO> getAllUsersDTO(PageRequest pageRequest, String username, String firstname, String lastname, String email);
    List<String> getAllUsernames();
    List<String> getAllEmails();
    int save(User users);
}
