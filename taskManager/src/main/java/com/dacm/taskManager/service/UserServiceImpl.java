package com.dacm.taskManager.service;

import com.dacm.taskManager.dao.UserRepository;
import com.dacm.taskManager.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public ResponseEntity<String> createUser(Users user) {
        // Intenta guardar el usuario en la base de datos
        try {
            Users savedUser = userRepository.save(user);
            // Si se guarda exitosamente, devuelve un ResponseEntity con un mensaje apropiado y un código de estado 201 (CREATED)
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado correctamente con ID: " + savedUser.getUser_id());
        } catch (Exception e) {
            // Si ocurre algún error al guardar el usuario, devuelve un ResponseEntity con un mensaje de error y un código de estado 500 (INTERNAL_SERVER_ERROR)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el usuario: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Users> getUserById(String userId) {
        return null;
    }

    @Override
    public ResponseEntity<String> updateUser(Integer userId, Users updatedUser) {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteUser(String userId) {
        return null;
    }
}
