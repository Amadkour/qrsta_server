package com.softkour.qrsta_server.config;

import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.repo.UserRepository;
import com.softkour.qrsta_server.security.JwtRequestFilter;

public class MyUtils {

    public static String getUserPhone(String logoutAndPhone){
        return  logoutAndPhone.substring(logoutAndPhone.indexOf('+')+1);
    }
    public static User getCurrentUserSession(UserRepository userRepository){
        return  userRepository.findUserByPhoneNumber(getUserPhone(JwtRequestFilter.username));
    }
}
