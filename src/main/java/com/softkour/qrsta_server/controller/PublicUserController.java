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
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.rest.pricing.v1.messaging.CountryReader;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

        Map<String, Object> responseMap = new HashMap<>();

        Twilio.init("ACbe5e5fed0c2829d18a709fd66de88ae9", "02e35f1788f444a866d56a2b13e3c008");

        PhoneNumber to = new PhoneNumber("+" + u.getCountryCode() + u.getPhoneNumber());
        PhoneNumber from = new PhoneNumber("+16593335662");
        String message = u.getOtp();
        MessageCreator creator = Message.creator(to, from, message);
        creator.create();

        System.out.println(u.getCountryCode() + u.getPhoneNumber());

        responseMap.put("otp", u.getOtp());
        return GenericResponse.success(responseMap);

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