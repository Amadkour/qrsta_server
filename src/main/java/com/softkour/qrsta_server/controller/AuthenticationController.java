package com.softkour.qrsta_server.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.softkour.qrsta_server.service.dto.UserLoginResponse;
import com.softkour.qrsta_server.service.mapper.UserLoginMapper;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta_server.config.Constants;
import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.repo.UserRepository;
import com.softkour.qrsta_server.request.LoginRequest;
import com.softkour.qrsta_server.request.RegisterationRequest;
import com.softkour.qrsta_server.security.JwtTokenUtil;
import com.softkour.qrsta_server.security.JwtUserDetailsService;
import com.softkour.qrsta_server.service.OTPService;

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
    public ResponseEntity<GenericResponse<Object>> loginUser(@RequestBody LoginRequest request) {
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
                UserLoginResponse userLoginResponse=UserLoginMapper.INSTANCE.toDto(user);
                userLoginResponse.setToken(token);
                responseMap.put("user",userLoginResponse );
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
        Supplier<String> otp = otpService.createRandomOneTimeOTP();
        user.setName(registerationRequest.getName());
        user.setMacAddress(registerationRequest.getMacAddress());
        user.setPhoneNumber(registerationRequest.getPhone());
        user.setPassword(new BCryptPasswordEncoder().encode(registerationRequest.getPassword()));
        user.setOtp(otp.get());
        user.setExpireOTPDateTime(Instant.now().plusSeconds(60));
        user.setType(registerationRequest.getUserType());
        user.setOrganization(registerationRequest.getOrganizationName());
        user.setDob(LocalDate.parse(registerationRequest.getBirthDate()));
//        user.setExpireOTPDateTime(LocalDateTime.now().plusMinutes(2));
        User u = userRepository.save(user);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("otp", u.getOtp());
        return GenericResponse.success(responseMap);
    }
    @PostMapping("/parent_register")
    public ResponseEntity<GenericResponse<Map<String, Object>>> createParent(
            @RequestBody RegisterationRequest registerationRequest) {
        User user = new User();
        Supplier<String> otp = otpService.createRandomOneTimeOTP();
        user.setName(registerationRequest.getName());
        user.setMacAddress(registerationRequest.getMacAddress());
        user.setPhoneNumber(registerationRequest.getPhone());
        user.setPassword(new BCryptPasswordEncoder().encode(registerationRequest.getPassword()));
        user.setOtp(otp.get());
        user.setExpireOTPDateTime(Instant.now().plusSeconds(60));
        user.setType(registerationRequest.getUserType());
        user.setOrganization(registerationRequest.getOrganizationName());
        user.setDob(LocalDate.parse(registerationRequest.getBirthDate()));
//        user.setExpireOTPDateTime(LocalDateTime.now().plusMinutes(2));
        User u = userRepository.save(user);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("otp", u.getOtp());
        return GenericResponse.success(responseMap);
    }

    @PostMapping("verfy_otp")
    public ResponseEntity<GenericResponse<Object>> verifyOtp(@RequestHeader String otp,
                                                             @RequestHeader String phone) {
        User user = userRepository.findUserByPhoneNumber(phone);
        Map<String, Object> responseMap = new HashMap<>();
        if (user.getOtp().equalsIgnoreCase(otp)) {
            // user.setOtp(null);
            user.setActive(true);
            userRepository.save(user);

            return GenericResponse.successWithMessageOnly("success verification");


        } else {
            responseMap.put("message", "Invalid Credentials");
            return GenericResponse.error(responseMap);
        }
    }


    @GetMapping("forget_password")
    public ResponseEntity<GenericResponse<Map<String, Object>>> forgetPassword(@RequestHeader String phone) {
        User user = userRepository.findUserByPhoneNumber(phone);
        Supplier<String> otp = otpService.createRandomOneTimeOTP();
        user.setOtp(otp.get());
        user.setExpireOTPDateTime(Instant.now().plusSeconds(60));
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("otp", user.getOtp());
        return GenericResponse.success(responseMap);
    }

    @GetMapping("change_password")
    public ResponseEntity<GenericResponse<String>> changePassword(@RequestHeader String phone,
                                                                  @RequestHeader String password, @RequestHeader String otp) {
        User user = userRepository.findUserByPhoneNumber(phone);
        try {
            if (user.getOtp().equalsIgnoreCase(otp) && user.getExpireOTPDateTime().isBefore(Instant.now())) {
                user.setPassword(new BCryptPasswordEncoder().encode(password));
            }
            return GenericResponse.successWithMessageOnly("success changed");
        } catch (Exception e) {
            return GenericResponse.successWithMessageOnly(e.getMessage());

        }
    }
}