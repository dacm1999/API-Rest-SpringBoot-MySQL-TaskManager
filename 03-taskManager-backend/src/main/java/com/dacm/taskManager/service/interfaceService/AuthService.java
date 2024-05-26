package com.dacm.taskManager.service.interfaceService;

import com.dacm.taskManager.responses.AuthResponse;
import com.dacm.taskManager.request.LoginRequest;
import com.dacm.taskManager.responses.RegisterRequest;
import jakarta.mail.MessagingException;

public interface AuthService {

    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request) throws MessagingException;


}
