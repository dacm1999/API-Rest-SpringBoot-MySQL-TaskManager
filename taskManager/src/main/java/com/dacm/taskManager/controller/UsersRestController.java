package com.dacm.taskManager.controller;

import com.dacm.taskManager.repository.UserRepository;
import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.model.AddModel;
import com.dacm.taskManager.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UsersRestController {

    private List<User> usersList;

    @Autowired
    private final UserService userService;
    @Autowired
    private final UserRepository userRepository;

    private List<User> usuariosAgregados = new ArrayList<>();
    private List<User> usuariosFallidos = new ArrayList<>();
    private boolean success = false;
    private String reason;


    @GetMapping("/allUsers")
    public ResponseEntity<List<User>> showAllUsers() {
        usersList = userService.getAllUsers();
        return ResponseEntity.ok(usersList);
    }

    @PostMapping("/")
    public ResponseEntity<AddModel> agregarUsuarios(@RequestBody User[] users) {
        int total = users.length;
        int num_added = 0;
        int num_failed = 0;
        usuariosAgregados = new ArrayList<>();
        usuariosFallidos = new ArrayList<>();


        // Ahora itera sobre cada usuario para determinar el éxito de la operación
        for (User usuario : users) {
            if (!userRepository.existsByEmail(usuario.getEmail())) {
                num_added++;
                usuariosAgregados.add(usuario);
                userService.save(users);
            } else {
                num_failed++;
                reason = "Email duplicated";
                usuariosFallidos.add(usuario);
            }
        }

        // Calcula el éxito de la operación y crea el modelo de respuesta
        if(num_added > 0){
            success = true;
        }else{
            success = false;
        }
        AddModel resultado = new AddModel(success, total, num_added, num_failed, (ArrayList) usuariosAgregados, (ArrayList) usuariosFallidos,reason);

        // Devuelve un ResponseEntity con el resultado y el código de estado HTTP OK
        return ResponseEntity.ok(resultado);

    }




}
