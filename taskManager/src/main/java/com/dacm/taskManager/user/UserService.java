package com.dacm.taskManager.user;

import com.dacm.taskManager.repository.UserRepository;
import com.dacm.taskManager.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService  {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private User user;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<List<User>> save(User[] users) {
        List<User> savedUsers = new ArrayList<>();
        for (User user : users) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            savedUsers.add(user);
        }
        userRepository.saveAll(savedUsers);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUsers);
    }

    @Transactional
    public UserResponse updateUser(UserRequest userRequest) {

        User user = User.builder()
                .userId(userRequest.userId)
                .username(userRequest.username)
                .firstname(userRequest.getFirstname())
                .lastname(userRequest.lastname)
                .email(userRequest.getEmail())
                .role(Role.ROLE_USER)
                .build();

        userRepository.updateUser(user.userId, user.firstname, user.lastname, user.email);

        return new UserResponse("El usuario se registró satisfactoriamente");
    }

    public UserDTO getUser(Integer id) {
        User user= userRepository.findById(id).orElse(null);

        if (user!=null)
        {
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


}


