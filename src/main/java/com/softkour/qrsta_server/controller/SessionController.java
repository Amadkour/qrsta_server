package com.softkour.qrsta_server.controller;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.entity.Course;
import com.softkour.qrsta_server.entity.Session;
import com.softkour.qrsta_server.payload.request.SessionCreationRequest;
import com.softkour.qrsta_server.service.CourseService;
import com.softkour.qrsta_server.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.time.Instant;

@RestController
@RequestMapping("/session/")
public class SessionController {
    @Autowired
    SessionService sessionService;
    @Autowired
    CourseService courseService;
    @GetMapping("course_sessions")
    public ResponseEntity<GenericResponse<Object>> getCourseSessions(@RequestHeader(name = "course_id") Long courseId){

       try {
            return GenericResponse.success(sessionService.findSessionsOfCourse(courseId));
        }catch (Exception e){
           return  GenericResponse.error(e.getMessage());
       }
    }
    @PostMapping("create")
    public ResponseEntity<GenericResponse<String>> createSession(@RequestBody @Valid SessionCreationRequest sessionCreationRequest){
        try{
            Session session = new Session();
            Course course = courseService.findOne(sessionCreationRequest.getCourseId()).orElseThrow(() -> new NotFoundException(String.valueOf(sessionCreationRequest.getCourseId())));
            session.setCourse(course);
            session.setStartDate(Instant.parse(sessionCreationRequest.getFromDate()));
            session.setEndDate(Instant.parse(sessionCreationRequest.getToDate()));
            sessionService.save(session);
            return GenericResponse.success("session add successfully");
        }catch (Exception e){
            return  GenericResponse.error(e.getMessage());
        }
    }
}
