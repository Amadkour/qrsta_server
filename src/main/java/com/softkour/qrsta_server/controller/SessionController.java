package com.softkour.qrsta_server.controller;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.Course;
import com.softkour.qrsta_server.entity.Session;
import com.softkour.qrsta_server.entity.SessionObject;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.payload.mapper.SessionDateAndStudentGradeMapper;
import com.softkour.qrsta_server.payload.mapper.SessionStudentsMapper;
import com.softkour.qrsta_server.payload.request.ObjectCreationRequest;
import com.softkour.qrsta_server.payload.request.SessionCreationRequest;
import com.softkour.qrsta_server.payload.response.SessionDetailsWithoutStudet;
import com.softkour.qrsta_server.repo.UserRepository;
import com.softkour.qrsta_server.service.CourseService;
import com.softkour.qrsta_server.service.SessionObjectService;
import com.softkour.qrsta_server.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.time.Instant;
import java.util.List;

@RestController
@Validated
@RequestMapping("/session/")
public class SessionController {
    @Autowired
    SessionService sessionService;
    @Autowired
    CourseService courseService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionObjectService sessionObjectService;

    @GetMapping("course_sessions")
    public ResponseEntity<GenericResponse<Object>> getCourseSessions(@RequestHeader(name = "course_id") Long courseId) {

        try {
            List<Session> sessionList = sessionService.findSessionsOfCourse(courseId);
            return GenericResponse.success(sessionList.stream().map(SessionDateAndStudentGradeMapper.INSTANCE::toDto));
        } catch (Exception e) {
            return GenericResponse.error(e.getMessage());
        }
    }

    @GetMapping("session_details_student")
    public ResponseEntity<GenericResponse<Object>> getSessionDetailsStudent(@RequestHeader(name = "session_id") Long sessionId) {

        try {
            Session session = sessionService.findOne(sessionId);
            return GenericResponse.success(SessionStudentsMapper.INSTANCE.toDTOs(session));
        } catch (Exception e) {
            return GenericResponse.error(e.getMessage());
        }
    }
    @GetMapping("take_attendance")
    public ResponseEntity<GenericResponse<Object>> takeCurrentUserInAttendance(@RequestHeader(name = "session_id") Long sessionId) {

        try {
            User u= MyUtils.getCurrentUserSession(userRepository);
            Session s= sessionService.addStudentToSession(u,sessionId);
            return GenericResponse.success(s);
        } catch (Exception e) {
            return GenericResponse.error(e.getMessage());
        }
    }
    @GetMapping("add_sudent_to_attendance")
    public ResponseEntity<GenericResponse<Object>> addStudentToAttendance(@RequestHeader(name = "session_id") Long sessionId,@RequestHeader(name = "student_id") Long userId) {

        try {
            User u= userRepository.findById(userId).orElseThrow(() -> new NotFoundException("student not found id: ".concat(String.valueOf(userId))));
                                sessionService.addStudentToSession(u,sessionId);
            return GenericResponse.success("add student to session successfully");
        } catch (Exception e) {
            return GenericResponse.error(e.getMessage());
        }
    }
    @GetMapping("remove_sudent_from_attendance")
    public ResponseEntity<GenericResponse<Object>> removeStudentFromAttendance(@RequestHeader(name = "session_id") Long sessionId,@RequestHeader(name = "student_id") Long userId) {

        try {
            User u= userRepository.findById(userId).orElseThrow(() -> new NotFoundException("student not found id: ".concat(String.valueOf(userId))));
            sessionService.removeStudentToSession(u,sessionId);
            return GenericResponse.success("remove student from session successfully");
        } catch (Exception e) {
            return GenericResponse.error(e.getMessage());
        }
    }
    @PostMapping("add_object")
    public ResponseEntity<GenericResponse<Object>> getSessionDetails(@RequestBody @Valid ObjectCreationRequest objectCreationRequest) {

        try {
             sessionObjectService.save(objectCreationRequest.getItem(),objectCreationRequest.getType(), objectCreationRequest.getParentId());
            return GenericResponse.successWithMessageOnly("add subItem Successfully");
        } catch (Exception e) {
            return GenericResponse.error(e.getMessage());
        }
    }


    @PostMapping("create")
    public ResponseEntity<GenericResponse<String>> createSession(@RequestBody @Valid SessionCreationRequest sessionCreationRequest) {
        try {
            Session session = new Session();
            Course course = courseService.findOne(sessionCreationRequest.getCourseId()).orElseThrow(() -> new NotFoundException(String.valueOf(sessionCreationRequest.getCourseId())));
            session.setCourse(course);
            session.setStartDate(Instant.parse(sessionCreationRequest.getFromDate()));
            session.setEndDate(Instant.parse(sessionCreationRequest.getToDate()));
            sessionService.save(session);
            return GenericResponse.success("session add successfully");
        } catch (Exception e) {
            return GenericResponse.error(e.getMessage());
        }
    }
}
