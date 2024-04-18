package com.dacm.taskManager.service.implementedService;

import com.dacm.taskManager.repository.UserRepository;
import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.service.interfaceService.UserService;
import com.dacm.taskManager.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstname(user.firstname);
        userDTO.setLastname(user.lastname);
        userDTO.setEmail(user.getEmail());

        return userDTO;
    }

    @Override
    public UserDTO getUser(String username) {
        User user = userRepository.findFirstByUsername(username);
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
    public Page<UserDTO> getAllUsersDTO(PageRequest pageRequest, String username, String firstname, String lastname, String email) {
        Specification<User> specification = Specification.where(null);
        if (username != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("username"), username));
        }
        if (firstname != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("firstname"), firstname));
        }
        if (lastname != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("lastname"), lastname));
        }
        if (email != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email));
        }

        Page<User> userPage = userRepository.findAll(specification, pageRequest);
        return userPage.map(this::convertToDTO);
    }

    @Override
    public List<String> getAllUsernames() {
        List<User> users = userRepository.findAll();
        List<String> usernames = users.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
        return usernames;
    }

    @Override
    public List<String> getAllEmails() {
        List<User> users = userRepository.findAll();
        List<String> emails = users.stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        return emails;
    }

    @Override
    public int save(User users) {

        Optional<User> existingUser = userRepository.findByUsername(users.getUsername());
        if (!existingUser.isPresent()) {
            // Si el usuario no existe, guárdalo en la base de datos
            userRepository.save(users);
            // Retorna un valor que indique que el usuario se guardó correctamente
            return 1;
        }
        // Retorna un valor que indique que el usuario ya existía y no se guardó
        return 0;
    }


}


