package com.softkour.qrsta_server.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.Course;
import com.softkour.qrsta_server.entity.StudentCourse;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.payload.request.AcceptRequest;
import com.softkour.qrsta_server.service.AuthService;
import com.softkour.qrsta_server.service.CourseService;

@RestController
@RequestMapping("/api/profile/")

public class ProfileServices {
    @Autowired
    CourseService courseService;
    @Autowired
    AuthService authService;

    @GetMapping("requests")
    public ResponseEntity<GenericResponse<List<Map<String, Object>>>> getAllRequestes() {
        User u = MyUtils.getCurrentUserSession(authService);
        List<Course> courses = courseService.getAllDisableStudentsOfCourses(u.getId());
        List<User> users = authService.getNeedToReplaceUsers(u.getId());

        List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
        for (Course c : courses) {
            for (StudentCourse s : c.getStudents()) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("type", "course");
                item.put("user_id", s.getStudent().getId());
                item.put("user_image", s.getStudent().getImageUrl());
                item.put("user_name", s.getStudent().getName());
                map.add(item);
            }
        }

        for (User user : users) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("type", "device");
            item.put("user_id", user.getId());
            item.put("user_image", user.getImageUrl());
            item.put("user_name", user.getName());
            map.add(item);
        }
        return GenericResponse.success(map);

    }

    @GetMapping("accept")
    public ResponseEntity<GenericResponse<Object>> accept(@RequestHeader("type") String type,
            @RequestHeader("data") List<AcceptRequest> data) {
        String message;
        if (type == "device") {
            message = authService.acceptToChangeDevice(data);
        } else {
            message = courseService.acceptToJoinCourse(data);

        }
        return GenericResponse.successWithMessageOnly(message);
    }

    @GetMapping("cancle_request")
    public ResponseEntity<GenericResponse<Object>> cancel(@RequestHeader("type") String type,
            @RequestHeader("data") List<AcceptRequest> data) {
        String message;
        if (type == "device") {
            message = authService.cancleRequest(data);
        } else {
            message = courseService.cancleRequest(data);

        }
        return GenericResponse.successWithMessageOnly(message);
    }

}
