package com.dacm.taskManager.serviceImpl;

import com.dacm.taskManager.dao.UserRepository;
import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private User user;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.user = new User();
    }



    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

//    @Override
//    public ResponseEntity<List<User>> save(User[] users) {
//        String encoderPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encoderPassword);
//        List<User> savedUsers = userRepository.saveAll(Arrays.asList(users));
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedUsers);
//    }

    @Override
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



}


