package com.softkour.qrsta_server.service;

import java.util.Random;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

@Service
public class OTPService {

    private final static Integer LENGTH = 4;

    public Supplier<String> createRandomOneTimeOTP() {
        return () -> {
            Random random = new Random();
            StringBuilder oneTimePassword = new StringBuilder();
            for (int i = 0; i < LENGTH; i++) {
                int randomNumber = random.nextInt(10);
                oneTimePassword.append(randomNumber);
            }
            return oneTimePassword.toString().trim();
        };
    }
}