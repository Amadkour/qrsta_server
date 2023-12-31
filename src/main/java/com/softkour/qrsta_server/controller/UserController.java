package com.softkour.qrsta_server.controller;


import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.repo.UserRepository;
import com.softkour.qrsta_server.request.ParentRegisterRequest;
import com.softkour.qrsta_server.security.JwtRequestFilter;
import com.softkour.qrsta_server.service.OTPService;
import com.softkour.qrsta_server.service.dto.UserLoginResponse;
import com.softkour.qrsta_server.service.mapper.UserLoginMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    OTPService otpService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @GetMapping("logout")
    public ResponseEntity<GenericResponse<Object>> logout() {
        User user = userRepository.findUserByPhoneNumber(MyUtils.getUserPhone(jwtRequestFilter.username));
        user.setLogoutTimes(user.getLogoutTimes() + 1);
        userRepository.save(user);
        return GenericResponse.successWithMessageOnly("logout successfully");
    }

    @PostMapping("/parent_register")
    public ResponseEntity<GenericResponse<Map<String, Object>>> createParent(
            @RequestBody ParentRegisterRequest parentRegisterRequest) {
        logger.warn(MyUtils.getUserPhone(JwtRequestFilter.username));
        User user = new User();
        Supplier<String> otp = otpService.createRandomOneTimeOTP();
        user.setName(parentRegisterRequest.getName());
        user.setPhoneNumber(parentRegisterRequest.getPhone());
        user.setOtp(otp.get());
        user.setExpireOTPDateTime(Instant.now().plusSeconds(60));
        user.setType(UserType.PARENT);
        User u = userRepository.save(user);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("otp", u.getOtp());
        return GenericResponse.success(responseMap);
    }

    @PostMapping("verfy_parent_otp")
    public ResponseEntity<GenericResponse<Object>> verifyOtp(@RequestHeader String otp,
                                                             @RequestHeader String parent_phone) {
        User parentUser = userRepository.findUserByPhoneNumber(parent_phone);
        Map<String, Object> responseMap = new HashMap<>();
        if (parentUser.getOtp().equalsIgnoreCase(otp)) {
            // user.setOtp(null);
            parentUser.setActive(true);
            userRepository.save(parentUser);

            User childUser = userRepository.findUserByPhoneNumber(MyUtils.getUserPhone(JwtRequestFilter.username));
            childUser.setParent(parentUser);
            userRepository.save(childUser);

            return GenericResponse.successWithMessageOnly("success verification");


        } else {
            responseMap.put("message", "Invalid Credentials");
            return GenericResponse.error(responseMap);
        }
    }
    @GetMapping("")
    public ResponseEntity<GenericResponse<Stream<UserLoginResponse>>> getUsers(){
       return GenericResponse.success(userRepository.findAll().stream().map(UserLoginMapper.INSTANCE::toDto));
    }
}
