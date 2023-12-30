package com.softkour.qrsta_server.controller;

import com.softkour.qrsta_server.config.Constants;
import com.softkour.qrsta_server.config.GenericResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.repo.UserRepository;
import com.softkour.qrsta_server.request.LoginRequest;
import com.softkour.qrsta_server.request.RegisterationRequest;
import com.softkour.qrsta_server.security.JwtTokenUtil;
import com.softkour.qrsta_server.service.JwtUserDetailsService;
import com.softkour.qrsta_server.service.OTPService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

@RestController
@RequestMapping("api/auth/")
public class AuthenticationController {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUserDetailsService userDetailsService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    OTPService otpService;

    @PostMapping("/login")
    public ResponseEntity<GenericResponse<Map<String, Object>>> loginUser(@RequestBody LoginRequest request) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            logger.warn(request.toString());
            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword()));
            User user = userRepository.findUserByPhoneNumber(request.getPhone());
            if (user == null) {
                responseMap.put("message", "user not found");
                return GenericResponse.errorWithCoder(responseMap, Constants.ERROR_USER_NOT_FOUND);
            } else if (!user.isActive()) {
                responseMap.put("message", "user not activate");
                return GenericResponse.errorWithCoder(responseMap, Constants.ERROR_USER_NOT_ACTIVATE);
            } else if (auth.isAuthenticated()) {
                logger.info("Logged In");
                userRepository.save(user);
                UserDetails userDetails = userDetailsService.loadUserByUsername(request.getPhone());
                String token = jwtTokenUtil.generateToken(userDetails);

                responseMap.put("user", user);
                responseMap.put("token", token);
                return GenericResponse.success(responseMap);
            } else {
                responseMap.put("message", "Invalid Credentials");
                return GenericResponse.error(responseMap);
            }
        } catch (DisabledException e) {
            responseMap.put("message", "User is disabled");
            return GenericResponse.error(responseMap);
        } catch (BadCredentialsException e) {
            responseMap.put("message", "Invalid Credentials");
            return GenericResponse.error(responseMap);
        } catch (Exception e) {
            responseMap.put("message", "Something went wrong");
            return GenericResponse.error(responseMap);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<GenericResponse<Map<String, Object>>> saveUser(
            @RequestBody RegisterationRequest registerationRequest) {
        User user = new User();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Supplier<String> otp = otpService.createRandomOneTimeOTP();
        user.setName(registerationRequest.getName());
        user.setMacAddress(registerationRequest.getMacAddress());
        user.setPhoneNumber(registerationRequest.getPhone());
        user.setPassword(bCryptPasswordEncoder.encode(registerationRequest.getPassword()));
        user.setOtp(otp.get());
        user.setType(registerationRequest.getUserType());
        user.setOrganization(registerationRequest.getOrganizationName());
        user.setDob(LocalDate.parse(registerationRequest.getBirthDate()));
        user.setExpireOTPDateTime(LocalDateTime.now().plusDays(30));
        user.setExpireOTPDateTime(LocalDateTime.now().plusMinutes(2));
        User u = userRepository.save(user);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("otp", u.getOtp());
        return GenericResponse.success(responseMap);
    }

    @PostMapping("verfy_otp")
    public ResponseEntity<GenericResponse<Map<String, Object>>> verifyOtp(@RequestHeader String otp, @RequestHeader String phone) {
        User user = userRepository.findUserByPhoneNumber(phone);
        Map<String, Object> responseMap = new HashMap<>();

        logger.warn(user.toString());
        if (Objects.equals(user.getOtp(), otp)) {
            user.setOtp(null);
            user.setLoggen(true);
            user.setActive(true);
            userRepository.save(user);
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setPassword(user.getPassword());
            loginRequest.setPhone(user.getPhoneNumber());
            return loginUser(loginRequest);
        } else {
            responseMap.put("message", "Invalid Credentials");
            return GenericResponse.error(responseMap);
        }
    }
}