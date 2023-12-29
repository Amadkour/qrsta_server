package com.softkour.qrsta_server.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.entity.enumeration.OrganizationType;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.repo.UserRepository;
import com.softkour.qrsta_server.request.LoginRequest;
import com.softkour.qrsta_server.request.RegisterationRequest;
import com.softkour.qrsta_server.security.JwtTokenUtil;
import com.softkour.qrsta_server.service.JwtUserDetailsService;
import com.softkour.qrsta_server.service.OTPService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
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
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword()));
            if (auth.isAuthenticated()) {
                logger.info("Logged In");
                UserDetails userDetails = userDetailsService.loadUserByUsername(request.getPhone());
                String token = jwtTokenUtil.generateToken(userDetails);
                responseMap.put("error", false);
                responseMap.put("message", "Logged In");
                responseMap.put("token", token);
                return ResponseEntity.ok(responseMap);
            } else {
                responseMap.put("error", true);
                responseMap.put("message", "Invalid Credentials");
                return ResponseEntity.status(401).body(responseMap);
            }
        } catch (DisabledException e) {
            e.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("message", "User is disabled");
            return ResponseEntity.status(500).body(responseMap);
        } catch (BadCredentialsException e) {
            responseMap.put("error", true);
            responseMap.put("message", "Invalid Credentials");
            return ResponseEntity.status(401).body(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("message", "Something went wrong");
            return ResponseEntity.status(500).body(responseMap);
        }
    }

    @PostMapping("/register")
    public String saveUser(@RequestBody RegisterationRequest registerationRequest) {
        User user = new User();
        logger.debug(registerationRequest.toString());
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
        // user.setLoggen(false);
        // user.setActive(false);
        user.setExpireOTPDateTime(LocalDateTime.now().plus(30, ChronoUnit.DAYS));
        user.setExpireOTPDateTime(LocalDateTime.now().plus(2, ChronoUnit.MINUTES));

        User u = userRepository.save(user);
        return u.getOtp();
        // Map<String, Object> responseMap = new HashMap<>();
        // UserDetails userDetails = userDetailsService.loadUserByUsername(phone);
        // String token = jwtTokenUtil.generateToken(userDetails);
        // responseMap.put("error", false);
        // responseMap.put("username", userName);
        // responseMap.put("message", "Account created successfully");
        // responseMap.put("token", token);
        // return ResponseEntity.ok(responseMap);
    }
}