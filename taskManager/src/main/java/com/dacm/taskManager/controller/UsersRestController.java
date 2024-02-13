package com.dacm.taskManager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UsersRestController {

    private List<String> usersList;

    @GetMapping("/test")
    public String testRols(){
        return "Test passed";
    }
}
