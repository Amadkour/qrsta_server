package com.softkour.qrsta_server.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.payload.request.LoginRequest;
import com.softkour.qrsta_server.payload.request.ParentRegisterRequest;
import com.softkour.qrsta_server.payload.request.RegisterationRequest;
import com.softkour.qrsta_server.payload.response.UserLoginResponse;
import com.softkour.qrsta_server.repo.UserRepository;
import com.softkour.qrsta_server.security.JwtTokenUtil;
import com.softkour.qrsta_server.security.JwtUserDetailsService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService {
    @Autowired
    UserRepository authorRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUserDetailsService userDetailsService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    OTPService otpService;

    public User register(RegisterationRequest request) {
        if (authorRepository.existsByPhoneNumber(request.getPhone())) {
            throw new ClientException("phone_number", "phone number already exists");
        } else if (authorRepository.existsByNationalId(request.getNationalId())) {
            throw new ClientException("national_id", "national_id number already exists");
        }
        return authorRepository.save(request.toUser(otpService));
    }

    public UserLoginResponse login(LoginRequest request) {
        try {
            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword()));
            User user = authorRepository.findUserByPhoneNumber(request.getPhone());

            if (user == null) {
                throw new ClientException("user", "user does not exist");
            } else if (!user.isActive()) {
                throw new ClientException("activation", "user does not Active");
            } else if (auth.isAuthenticated()) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(request.getPhone());
                String token = jwtTokenUtil.generateToken(userDetails);
                UserLoginResponse userLoginResponse = user.toUserLoginResponse();
                userLoginResponse.setToken(token);
                return userLoginResponse;
            } else {
                throw new ClientException("user", "Invalid Credentials");
            }
        } catch (DisabledException e) {
            throw new ClientException("invalid_Credentials", "User is disabled");
        } catch (BadCredentialsException e) {
            throw new ClientException("invalid_credentials", "Invalid Credentials");
        }
    }

    public User createParent(ParentRegisterRequest request) {
        if (authorRepository.existsByPhoneNumber(request.getPhone())) {
            throw new ClientException("phone_number", "phone number already exists");
        }
        return authorRepository.save(request.toUser(otpService));

    }

    public User getUserByPhoneNumber(String phoneNumber) {
        return authorRepository.findUserByPhoneNumber(phoneNumber);
    }

    public User save(User user) {
        return authorRepository.save(user);
    }

    public List<User> getAllAsAbstract() {
        return authorRepository.findAll();
    }

    public void verifyUser(String otp) {
    }

    public User verifyParentUser(String otp, String parentPhone, User currentUser) {
        User parentUser = getUserByPhoneNumber(parentPhone);
        if (parentUser.getOtp().equalsIgnoreCase(otp) && Instant.now().isBefore(parentUser.getExpireOTPDateTime())) {
            parentUser.setActive(true);
            parentUser = save(parentUser);
            MyUtils.getCurrentUserSession(null);
            currentUser.setParent(parentUser);
            save(currentUser);
            return parentUser;
        } else {
            throw new ClientException("user", "Invalid Credentials");
        }
    }

    public User getUserById(Long userId) {
        return authorRepository.findById(userId)
                .orElseThrow(
                        () -> new ClientException("student", "student not found id: ".concat(String.valueOf(userId))));
    }
}
