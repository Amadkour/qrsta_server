package com.softkour.qrsta_server.controller;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.entity.Session;
import com.softkour.qrsta_server.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/session/")
public class SessionController {
    @Autowired
    SessionService sessionService;
    @GetMapping("course_sessions")
    public ResponseEntity<GenericResponse<Object>> getCourseSessions(@RequestHeader(name = "course_id") Long courseId){

       try {
            return GenericResponse.success(sessionService.findSessionsOfCourse(courseId));
        }catch (Exception e){
           return  GenericResponse.error(e.getMessage());
       }
    }
}
