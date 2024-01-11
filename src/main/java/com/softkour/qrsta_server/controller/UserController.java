package com.softkour.qrsta_server.controller;

import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.payload.request.ParentRegisterRequest;
import com.softkour.qrsta_server.payload.response.AbstractUser;
import com.softkour.qrsta_server.security.JwtRequestFilter;
import com.softkour.qrsta_server.service.AuthService;
import com.softkour.qrsta_server.service.OTPService;

import io.lettuce.core.dynamic.annotation.Param;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    OTPService otpService;
    @Autowired
    AuthService userService;
    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @GetMapping("logout")
    public ResponseEntity<GenericResponse<Object>> logout() {
        User user = MyUtils.getCurrentUserSession(userService);
        user.setLogoutTimes(user.getLogoutTimes() + 1);
        userService.save(user);
        return GenericResponse.successWithMessageOnly("logout successfully");
    }

    @PostMapping("/parent_register")
    public ResponseEntity<GenericResponse<Object>> createParent(
            @RequestBody ParentRegisterRequest parentRegisterRequest) {
        User user = userService.createParent(parentRegisterRequest);
        return GenericResponse
                .successWithMessageOnly(user.getOtp().toString());
    }

    @PostMapping("verfy_parent_otp")
    public ResponseEntity<GenericResponse<Object>> verifyOtp(@RequestHeader("parent_otp") String otp,
            @RequestHeader @Param("parent_phone_number") String parentPhone) {
        userService.verifyParentUser(otp, parentPhone, MyUtils.getCurrentUserSession(userService));
        return GenericResponse.successWithMessageOnly("Yor parent created Successffly");
    }

    @GetMapping("all")
    public ResponseEntity<GenericResponse<Stream<AbstractUser>>> getUsers() {
        return GenericResponse.success(userService.getAllAsAbstract().stream().map((e) -> e.toAbstractUser()));
    }
}
