package com.softkour.qrsta_server.controller;


import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.repo.UserRepository;
import com.softkour.qrsta_server.security.JwtRequestFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtRequestFilter jwtRequestFilter;
    @GetMapping("logout")
    public ResponseEntity<GenericResponse<Object>> logout(){
        User user=userRepository.findUserByPhoneNumber(MyUtils.getUserPhone(jwtRequestFilter.username));
      user.setLogoutTimes(user.getLogoutTimes()+1);
      userRepository.save(user);
       return GenericResponse.successWithMessageOnly("logout successfully");
    }
}
