package com.dacm.taskManager.controller;

import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.model.AddModel;
import com.dacm.taskManager.user.Role;
import com.dacm.taskManager.user.UserDTO;
import com.dacm.taskManager.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.HibernateQueryException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UsersRestController {


    @Autowired
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Integer id) {
        UserDTO userDTO = userService.getUser(id);
        if (userDTO == null) {
            throw new CommonErrorResponse("User not found with ID: " + id);
        }
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping(value = "username/{username}")
    public ResponseEntity<UserDTO> getByUsername(@PathVariable String username){
        UserDTO userDTO = userService.getUser(username);
        if(userDTO == null) {
            throw new CommonErrorResponse("Username not found " + username);
        }
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable Integer id, @RequestBody UserDTO updatedUserDTO) {
        try {
            UserDTO updatedUser = userService.updateUserById(id, updatedUserDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (NoSuchElementException e) {
            // Manejar el caso en que el usuario no se encuentre
            throw new CommonErrorResponse("User not found with ID: " + id);
        } catch (Exception e) {
            // Manejar otros posibles errores
            throw new CommonErrorResponse("Error updating user with ID: " + id, e);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserDTO> deleteUserById(@PathVariable Integer id) {
        try {
            UserDTO deletedUserById = userService.deleteUserById(id);
            return ResponseEntity.ok(deletedUserById);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<AddModel> addManyUsers(@RequestBody User[] users) {
        AddModel resultado = null;

        try{
            List<UserDTO> addedUsers = new ArrayList<>();
            List<UserDTO> usersFail = new ArrayList<>();
            String reason = "";

            Set<String> existingUsernames = new HashSet<>();
            Set<String> existingEmails = new HashSet<>();

            // Iterar sobre cada usuario para determinar el éxito de la operación
            for (User usuario : users) {
                String username = usuario.getUsername();
                String email = usuario.getEmail();

                // Verificar si el nombre de usuario ya existe
                if (existingUsernames.contains(username)) {
                    // Agregar el usuario al listado de usuarios fallidos
                    usersFail.add(UserDTO.builder()
                            .username(usuario.getUsername())
                            .firstname(usuario.getFirstname())
                            .lastname(usuario.getLastname())
                            .email(usuario.getEmail())
                            .build());
                    reason = "Username duplicated";
                    continue; // Pasar al siguiente usuario
                }

                // Verificar si el correo electrónico ya existe
                if (existingEmails.contains(email)) {
                    // Agregar el usuario al listado de usuarios fallidos
                    usersFail.add(UserDTO.builder()
                            .username(usuario.getUsername())
                            .firstname(usuario.getFirstname())
                            .lastname(usuario.getLastname())
                            .email(usuario.getEmail())
                            .build());
                    reason = "Email duplicated";
                    continue; // Pasar al siguiente usuario
                }

                // Asegurarse de que el campo 'role' no sea nulo
                Role role = (usuario.getRole() != null) ? usuario.getRole() : Role.ROLE_USER;

                // Construir el objeto User con el campo 'role' asignado
                User user = User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(usuario.getPassword()))
                        .firstname(usuario.getFirstname())
                        .lastname(usuario.getLastname())
                        .email(email)
                        .role(role) // Asignar el campo 'role'
                        .build();

                // Guardar el usuario en la base de datos
                userService.save(new User[]{user});

                // Agregar el usuario a los conjuntos de nombres de usuario y correos electrónicos existentes
                existingUsernames.add(username);
                existingEmails.add(email);

                // Convertir el usuario a UserDTO y agregarlo a la lista de usuarios agregados
                addedUsers.add(UserDTO.builder()
                        .username(username)
                        .firstname(usuario.getFirstname())
                        .lastname(usuario.getLastname())
                        .email(email)
                        .build());
            }

            int total = users.length;
            int num_added = addedUsers.size();
            int num_failed = usersFail.size();

            // Calcular el éxito de la operación y crear el modelo de respuesta
            boolean success = num_added > 0;
            resultado = new AddModel(success, total, num_added, num_failed, (ArrayList) addedUsers, (ArrayList) usersFail, reason);

            // Devolver un ResponseEntity con el resultado y el código de estado HTTP OK
        }catch (HibernateQueryException e){
            throw new CommonErrorResponse("Duplicated values", e);
        }
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<UserDTO>> showAllUsers() {
        List<UserDTO> userDTOList = userService.getAllUsersDTO();
        return ResponseEntity.ok(userDTOList);
    }

}
