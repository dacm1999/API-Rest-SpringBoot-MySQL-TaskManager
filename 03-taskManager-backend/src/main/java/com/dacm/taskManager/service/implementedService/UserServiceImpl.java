package com.dacm.taskManager.service.implementedService;

import com.dacm.taskManager.enums.Role;
import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.repository.UserRepository;
import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.responses.AddedResponse;
import com.dacm.taskManager.responses.UserErrorResponse;
import com.dacm.taskManager.responses.UserPaginationResponse;
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
    public AddedResponse addMultipleUsers(User[] users) {
        List<UserDTO> addedUsers = new ArrayList<>();
        List<UserErrorResponse> usersFail = new ArrayList<>();
        String reason = "";
        String errorDescription = "";

        Set<String> existingUsernames = new HashSet<>();
        Set<String> existingEmails = new HashSet<>();

        // Obtener todos los nombres de usuario y correos electrónicos existentes en la base de datos
        List<String> usernames = getAllUsernames();
        List<String> emails = getAllEmails();
        existingUsernames.addAll(usernames);
        existingEmails.addAll(emails);

        // Iterar sobre cada usuario para determinar el éxito de la operación
        for (User usuario : users) {
            String username = usuario.getUsername();
            String email = usuario.getEmail();

            reason = "Could not add this users  ";
            errorDescription = "Username duplicated";
            // Verificar si el nombre de usuario ya existe
            if (existingUsernames.contains(username)) {
                // Agregar el usuario al listado de usuarios fallidos
                usersFail.add(new UserErrorResponse(username, email, errorDescription));
                continue; // Pasar al siguiente usuario
            }

            // Verificar si el correo electrónico ya existe
            if (existingEmails.contains(email)) {
                // Agregar el usuario al listado de usuarios fallidos
                errorDescription = "Email duplicated";
                usersFail.add(new UserErrorResponse(username, email, errorDescription));
                continue; // Pasar al siguiente usuario
            }

            // Construir el objeto User
            User user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(usuario.getPassword()))
                    .firstname(usuario.getFirstname())
                    .lastname(usuario.getLastname())
                    .email(email)
                    .role(Role.ROLE_USER) // Asignar el rol del usuario
                    .build();

            // Guardar el usuario en la base de datos
            save(user);

            // Agregar el usuario a los conjuntos de nombres de usuario y correos electrónicos existentes
            reason = "Could not add this users";
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
        return new AddedResponse(success, total, num_added, num_failed, (ArrayList) addedUsers, (ArrayList) usersFail, reason);
    }

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
        if(user == null) {
            throw new CommonErrorResponse("Username not found: " + username);
        }
        return convertToDTO(user);
    }

    @Override
    public UserDTO getUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CommonErrorResponse("User not found with ID: " + id));
        return convertToDTO(user);
    }

    @Override
    public UserDTO updateUserById(Integer id, UserDTO updatedUserDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));

        user.setUsername(updatedUserDTO.getUsername());
        user.setFirstname(updatedUserDTO.getFirstname());
        user.setLastname(updatedUserDTO.getLastname());
        user.setEmail(updatedUserDTO.getEmail());

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    @Override
    public UserDTO deleteUserById(Integer id) {

        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));

        userRepository.deleteById(id);

        return convertToDTO(user);
    }

    @Override
    public UserPaginationResponse getAllUsersDTO(PageRequest pageRequest, String username, String firstname, String lastname, String email) {
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
        Page<UserDTO> userDTOPage = userPage.map(this::convertToDTO);

        UserPaginationResponse response = new UserPaginationResponse();
        response.setUsers(userDTOPage.getContent());
        response.setTotalPages(userDTOPage.getTotalPages());
        response.setTotalElements(userDTOPage.getTotalElements());
        response.setNumber(userDTOPage.getNumber());
        response.setNumberOfElements(userDTOPage.getNumberOfElements());
        response.setSize(userDTOPage.getSize());

        return response;
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


