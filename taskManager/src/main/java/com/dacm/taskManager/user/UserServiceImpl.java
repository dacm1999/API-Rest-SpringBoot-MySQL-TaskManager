package com.dacm.taskManager.user;

import com.dacm.taskManager.repository.UserRepository;
import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private User user;


    @Override
    public UserDTO convertToDTO(User user) {
        // Aquí realizas la conversión de un objeto User a un UserDTO
        // Por ejemplo, puedes copiar los campos relevantes de User a UserDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstname(user.firstname);
        userDTO.setLastname(user.lastname);
        userDTO.setEmail(user.getEmail());
        // Añade más asignaciones según los campos de tu UserDTO

        return userDTO;
    }

    @Override
    public UserDTO getUser(Integer id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            UserDTO userDTO = UserDTO.builder()
                    .username(user.username)
                    .firstname(user.firstname)
                    .lastname(user.lastname)
                    .email(user.email)
                    .build();
            return userDTO;
        }
        return null;
    }

    @Override
    public UserDTO updateUserById(Integer id, UserDTO updatedUserDTO) {
        // Verify if user exits
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));

        // Update user fields with new values
        user.setUsername(updatedUserDTO.getUsername());
        user.setFirstname(updatedUserDTO.getFirstname());
        user.setLastname(updatedUserDTO.getLastname());
        user.setEmail(updatedUserDTO.getEmail());

        // Save all changes at repository
        User updatedUser = userRepository.save(user);

        // Convert all updated users a DTO and return them
        return convertToDTO(updatedUser);
    }

    @Override
    public UserDTO deleteUserById(Integer id) {

        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));

        userRepository.deleteById(id);

        return convertToDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsersDTO() {

        List<User> usersList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();

        for (User users : usersList) {

            UserDTO userDTO = convertToDTO(users);
            userDTOList.add(userDTO);

        }

        return userDTOList;
    }

    @Override
    public List<UserDTO> addUsers(List<User> userList) {
        List<UserDTO> addedUsers = new ArrayList<>();
        for (User user : userList) {
            Boolean userExists = userRepository.existsByEmail(user.getEmail());
            if (!userExists) {
                // Guardar el nuevo usuario en la base de datos
                userRepository.save(user);

                // Convertir el usuario guardado a DTO y agregarlo a la lista de usuarios agregados
                addedUsers.add(convertToDTO(user));
            }
        }
        return addedUsers;
    }


    @Override
    public ResponseEntity<List<User>> save(User[] users) {
        List<User> savedUsers = userRepository.saveAll(Arrays.asList(users));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUsers);
    }


}


