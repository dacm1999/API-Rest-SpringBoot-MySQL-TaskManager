package com.dacm.taskManager.service.interfaceService;

import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.responses.PasswordRequest;

public interface PasswordResetService {

    public void resetPassword(PasswordRequest request);
    String generateRandomPassword(int lenght);

}
