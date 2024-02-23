package com.dacm.taskManager.service;

import com.dacm.taskManager.responses.AuthResponse;
import com.dacm.taskManager.responses.LoginRequest;
import com.dacm.taskManager.responses.RegisterRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);

}
