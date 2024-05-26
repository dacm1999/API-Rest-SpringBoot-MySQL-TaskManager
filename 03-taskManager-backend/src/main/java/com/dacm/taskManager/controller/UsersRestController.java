package com.dacm.taskManager.controller;

import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.responses.AddedResponse;
import com.dacm.taskManager.responses.UserErrorResponse;
import com.dacm.taskManager.enums.Role;
import com.dacm.taskManager.dto.UserDTO;
import com.dacm.taskManager.responses.UserPaginationResponse;
import com.dacm.taskManager.service.implementedService.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.HibernateQueryException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UsersRestController {

    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "Create multiple users", description = "Create multiple users in the database")
    @ApiResponse(responseCode = "200", description = "Users created successfully")
    @PostMapping("/createMultiple")
    public ResponseEntity<AddedResponse> addManyUsers(@RequestBody User[] users) {
        AddedResponse result = null;
        try {
            result = userService.addMultipleUsers(users);
        } catch (HibernateQueryException e) {
            throw new CommonErrorResponse("Duplicated values", e);
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary="Get user by ID", description = "Get user by ID from the database")
    @ApiResponse(responseCode = "200", description = "User found")
    @GetMapping(value = "info-id/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Integer id) {
        UserDTO userDTO = userService.getUser(id);
        return ResponseEntity.ok(userDTO);
    }

    @Operation(summary="Get user by username", description = "Get user by username from the database")
    @ApiResponse(responseCode = "200", description = "User found")
    @GetMapping(value = "info-username/{username}")
    public ResponseEntity<UserDTO> getByUsername(@PathVariable String username) {
        UserDTO userDTO = userService.getUser(username);
        return ResponseEntity.ok(userDTO);
    }

    @Operation(summary = "Show all users", description = "Show all users in the database")
    @ApiResponse(responseCode = "200", description = "Users found")
    @GetMapping("/allUsers")
    public ResponseEntity<UserPaginationResponse> showAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) String email
    ) {
        UserPaginationResponse response = userService.getAllUsersDTO(PageRequest.of(page, size), username, firstname, lastname, email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update user by ID", description = "Update user by ID in the database")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @PutMapping(value = "update/{id}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable Integer id, @RequestBody UserDTO updatedUserDTO) {
        try {
            UserDTO updatedUser = userService.updateUserById(id, updatedUserDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (NoSuchElementException e) {
            throw new CommonErrorResponse("User not found with ID: " + id);
        } catch (Exception e) {
            throw new CommonErrorResponse("Error updating user with ID: " + id, e);
        }
    }

    @Operation(summary = "Delete user by ID", description = "Delete user by ID in the database")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @DeleteMapping(value = "delete/{id}")
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

}
