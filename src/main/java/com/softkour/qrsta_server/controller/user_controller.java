package com.softkour.qrsta.controller;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta.domain.enumeration.Type;
import com.softkour.qrsta.entity.Course;
import com.softkour.qrsta.entity.User;
import com.softkour.qrsta.repository.CourseRepository;
import com.softkour.qrsta.repository.UserRepository;

@RestController
@RequestMapping("/api/")
public class user_controller {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CourseRepository courseRepository;

    @GetMapping("register")
    public User dummy() {
        User u = new User();
        u.setDob(ZonedDateTime.now());
        u.setName("Ahmed Madkour");
        u.setPassword("Aa@@aA2291996");
        u.setMacAddress("qwwwwwwww");
        u.setPhoneNumber("01110672232");
        u.setOrganization("lll");
        u.setType(Type.STUDENT);

        // u.setId(1L);
        return userRepository.save(u);

        // Course c = new Course();
        // c.name("lll");
        // return courseRepository.save(c);
    }
}
