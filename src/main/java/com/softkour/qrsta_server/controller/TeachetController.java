package com.softkour.qrsta_server.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.OidcProviderConfig.UpdateRequest;
import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.user.Teacher;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.payload.request.ParentRegisterRequest;
import com.softkour.qrsta_server.payload.request.RegisterationRequest;
import com.softkour.qrsta_server.payload.request.UpdateUserRequest;
import com.softkour.qrsta_server.payload.response.AbstractChild;
import com.softkour.qrsta_server.payload.response.AbstractUser;
import com.softkour.qrsta_server.security.JwtRequestFilter;
import com.softkour.qrsta_server.service.AuthService;
import com.softkour.qrsta_server.service.OTPService;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/api/teacher/")
public class TeachetController {
    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    OTPService otpService;
    @Autowired
    AuthService userService;
    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @GetMapping("update_payment_account")
    public ResponseEntity<GenericResponse<Object>> setPaymentAccount(@RequestHeader("account_id") String accountId) {
        User u = MyUtils.getCurrentUserSession(userService);
        Teacher t = u.getTeacher();
        t.setPaymentaccount(accountId);
        userService.save(u);
        return GenericResponse.successWithMessageOnly("success updating");
    }

    @GetMapping("change_payment_mode")
    public ResponseEntity<GenericResponse<Object>> setPaymentMode(@RequestHeader("mode") Boolean mode) {
        User u = MyUtils.getCurrentUserSession(userService);
        Teacher t = u.getTeacher();
        t.setUsePayment(mode);
        userService.save(u);
        return GenericResponse.successWithMessageOnly("success updating payment mode to: " + mode);
    }

    @GetMapping("change_absence_mode")
    public ResponseEntity<GenericResponse<Object>> setAbsenceMode(@RequestHeader("mode") Boolean mode) {
        User u = MyUtils.getCurrentUserSession(userService);
        Teacher t = u.getTeacher();
        t.setEnableAbsence(mode);
        userService.save(u);
        return GenericResponse.successWithMessageOnly("success updating absence mode to: " + mode);
    }

    @GetMapping("change_device_mode")
    public ResponseEntity<GenericResponse<Object>> setSwitchDeviceMode(@RequestHeader("mode") Boolean mode) {
        User u = MyUtils.getCurrentUserSession(userService);
        Teacher t = u.getTeacher();
        t.setEnableAutoChangeDevice(mode);
        userService.save(u);
        return GenericResponse.successWithMessageOnly("success updating swich device mode to: " + mode);
    }

    @GetMapping("change_join_mode")
    public ResponseEntity<GenericResponse<Object>> setJoinMode(@RequestHeader("mode") Boolean mode) {
        User u = MyUtils.getCurrentUserSession(userService);
        Teacher t = u.getTeacher();
        t.setEnableAutoChangeDevice(mode);
        userService.save(u);
        return GenericResponse.successWithMessageOnly("success updating join mode to: " + mode);
    }

    @GetMapping("get_settings")
    public ResponseEntity<GenericResponse<Object>> setJoinMode() {
        User u = MyUtils.getCurrentUserSession(userService);
        Map<String, Boolean> m = new HashMap<String, Boolean>();
        m.put("device_mode", u.getTeacher().isEnableAutoChangeDevice());
        m.put("join_mode", u.getTeacher().isEnableAutojoin());
        m.put("payment_mode", u.getTeacher().isUsePayment());
        m.put("absence_mode", u.getTeacher().isEnableAbsence());
        return GenericResponse.success(m);
    }
}
