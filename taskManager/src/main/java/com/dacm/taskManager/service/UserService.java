package com.dacm.taskManager.service;

import com.dacm.taskManager.entity.Users;
import org.springframework.http.ResponseEntity;

public interface UserService {

    // Método para crear un nuevo usuario
    ResponseEntity<String> createUser(Users user);

    // Método para obtener un usuario por su ID
    ResponseEntity<Users> getUserById(String userId);

    // Método para actualizar un usuario existente
    ResponseEntity<String> updateUser(Integer userId, Users updatedUser);

    // Método para eliminar un usuario por su ID
    ResponseEntity<String> deleteUser(String userId);


}
