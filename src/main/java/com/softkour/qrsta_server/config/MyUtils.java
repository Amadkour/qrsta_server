package com.softkour.qrsta_server.config;

import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.security.JwtRequestFilter;
import com.softkour.qrsta_server.service.AuthService;

public class MyUtils {

    public static String getUserPhone(String logoutAndPhone) {
        return logoutAndPhone.substring(logoutAndPhone.indexOf('+') + 1);
    }

    public static User getCurrentUserSession(AuthService authService) {
        return authService.getUserByPhoneNumber(getUserPhone(JwtRequestFilter.username));
    }
}
