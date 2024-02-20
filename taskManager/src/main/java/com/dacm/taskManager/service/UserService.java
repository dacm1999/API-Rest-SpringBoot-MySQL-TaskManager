package com.dacm.taskManager.service;

import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.user.UserDTO;
import com.dacm.taskManager.user.UserRequest;
import com.dacm.taskManager.user.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    UserDTO convertToDTO(User user);

    UserDTO getUser(String username);

    UserDTO getUser(Integer id);

    UserDTO updateUserById(Integer id, UserDTO updatedUserDTO);

    UserDTO deleteUserById(Integer id);
    List<UserDTO> getAllUsersDTO();
    ResponseEntity<List<User>> save(User[] users);

}
