package com.softkour.qrsta_server.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.payload.request.LoginRequest;
import com.softkour.qrsta_server.payload.request.RegisterationRequest;
import com.softkour.qrsta_server.repo.CountryRepo;
import com.softkour.qrsta_server.repo.UserRepository;
import com.softkour.qrsta_server.service.AuthService;
import com.softkour.qrsta_server.service.OTPService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/auth/")
@Validated
@Slf4j
public class PublicUserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CountryRepo countryRepo;
    @Autowired
    AuthService authService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    OTPService otpService;

    @GetMapping("country")
    public ResponseEntity<GenericResponse<Object>> dummy() {
        return GenericResponse.success(countryRepo.findAll().stream().map(e -> e.toCountryResponce()).toList());
    }

    @PostMapping("login")
    public ResponseEntity<GenericResponse<Object>> loginUser(@RequestBody LoginRequest request) {

        return GenericResponse.success(authService.login(request));

    }

    @PostMapping("register")
    public ResponseEntity<GenericResponse<Object>> saveUser(
            @RequestBody @Valid RegisterationRequest registerationRequest) {
        User u = authService.register(registerationRequest);
        log.warn(registerationRequest.getPhone());
        log.warn(u.getPhoneNumber());
        log.warn(u.getOtp());
        boolean result = authService.sendMessageToPhone(u.getPhoneNumber(), u.getOtp());
        if (result)
            return GenericResponse
                    .successWithMessageOnly("send code successfully to " + u.getPhoneNumber());
        else
            return GenericResponse.errorWithMessageOnly(
                    "faild to send otp to your phoneto " + u.getPhoneNumber());
    }

    @PostMapping("resend")
    public ResponseEntity<GenericResponse<Object>> resendOtp(@RequestHeader("phone") String phoneNumber) {
        User u = authService.getUserByPhoneNumber(phoneNumber);
        boolean result = authService.sendMessageToPhone(u.getPhoneNumber(), u.getOtp());
        if (result)
            return GenericResponse
                    .successWithMessageOnly("send code successfullyto " + u.getPhoneNumber());
        else
            return GenericResponse.errorWithMessageOnly(
                    "faild to send otp to your phone " + u.getPhoneNumber());
    }
    @PostMapping("verfy_otp")
    public ResponseEntity<GenericResponse<Object>> verifyOtp(@Valid @RequestHeader("user_otp") String otp,
            @Valid @RequestHeader("phone") String phone) {
        User user = userRepository.findUserByPhoneNumber(phone);
        Map<String, Object> responseMap = new HashMap<>();
        if (user.getOtp().equalsIgnoreCase(otp)) {
            // user.setOtp(null);
            user.setActive(true);
            userRepository.save(user);

            return GenericResponse.successWithMessageOnly("success_verification");

        } else {
            responseMap.put("message", "invalid_OTP");
            return GenericResponse.errorOfMap(responseMap);
        }
    }

    @GetMapping("forget_password")
    public ResponseEntity<GenericResponse<Map<String, Object>>> forgetPassword(@RequestHeader("phone") String phone,
            @RequestHeader("phone_code") String phoneCode) {
        User user = userRepository.findUserByPhoneNumber(phone);
        Supplier<String> otp = otpService.createRandomOneTimeOTP();
        user.setOtp(otp.get());
        user.setExpireOTPDateTime(Instant.now().plusSeconds(60));
        userRepository.save(user);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("otp", user.getOtp());
        return GenericResponse.success(responseMap);
    }

    @GetMapping("change_password")
    public ResponseEntity<GenericResponse<Object>> changePassword(@RequestHeader("phone") String phone,
            @RequestHeader("phone_code") String phoneCode,
            @RequestHeader("password") String password,
            @RequestHeader("otp") String otp) {
        User user = userRepository.findUserByPhoneNumber(phone);
        if (user.getOtp().equalsIgnoreCase(otp) && !user.getExpireOTPDateTime().isBefore(Instant.now())) {
                user.setPassword(new BCryptPasswordEncoder().encode(password));
                userRepository.save(user);
                return GenericResponse.successWithMessageOnly("success changed");

            }
            log.warn(user.getOtp());
            log.warn(String.valueOf(user.getExpireOTPDateTime().isBefore(Instant.now())));
            return GenericResponse.errorWithMessageOnly("error in update");

    }
}