package com.dacm.taskManager.service.interfaceService;

import com.dacm.taskManager.request.PasswordResetRequest;

public interface PasswordService {

    void resetPassword(PasswordResetRequest request);
    void updatePassword(String username, String newPassword);
    String generateRandomPassword(int length);
}
