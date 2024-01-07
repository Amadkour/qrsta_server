package com.softkour.qrsta_server.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.Course;
import com.softkour.qrsta_server.entity.Post;
import com.softkour.qrsta_server.entity.Session;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.payload.request.ObjectCreationRequest;
import com.softkour.qrsta_server.payload.request.SessionCreationRequest;
import com.softkour.qrsta_server.payload.response.SessionAndSocialResponce;
import com.softkour.qrsta_server.service.AuthService;
import com.softkour.qrsta_server.service.CourseService;
import com.softkour.qrsta_server.service.PostService;
import com.softkour.qrsta_server.service.SessionObjectService;
import com.softkour.qrsta_server.service.SessionService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Validated
@Slf4j
@RequestMapping("/api/session/")
public class SessionController {
    @Autowired
    SessionService sessionService;
    @Autowired
    PostService postService;
    @Autowired
    CourseService courseService;
    @Autowired
    AuthService authService;
    @Autowired
    SessionObjectService sessionObjectService;

    @GetMapping("course_sessions")
    public ResponseEntity<GenericResponse<Object>> getCourseSessions(@RequestHeader(name = "course_id") Long courseId) {

        List<Session> sessionList = sessionService.findSessionsOfCourse(courseId);
        List<Post> postslist = postService.posts(courseId);
        log.warn(postslist.stream().map((e) -> e.getLikes()).toList().toString());
        return GenericResponse.success(
                new SessionAndSocialResponce(
                        sessionList.stream().map((e) -> e.toSessionDateAndStudentGrade()).toList(),
                        postslist.stream().map((e) -> e.toPostResponce(sessionService, authService)).toList())

        );

    }

    @GetMapping("session_details_student")
    public ResponseEntity<GenericResponse<Object>> getSessionDetailsStudent(
            @RequestHeader(name = "session_id") Long sessionId) {

        try {
            Session session = sessionService.findOne(sessionId);
            return GenericResponse.success(session.toSessionDetailsStudent());
        } catch (Exception e) {
            return GenericResponse.errorOfException(e);
        }
    }

    @GetMapping("take_attendance")
    public ResponseEntity<GenericResponse<Object>> takeCurrentUserInAttendance(
            @RequestHeader(name = "session_id") Long sessionId) {

        try {
            User u = MyUtils.getCurrentUserSession(authService);
            sessionService.addStudentToSession(u, sessionId);
            return GenericResponse.successWithMessageOnly("take you successfully");
        } catch (Exception e) {
            return GenericResponse.errorOfException(e);
        }
    }

    @GetMapping("add_student_to_attendance")
    public ResponseEntity<GenericResponse<Object>> addStudentToAttendance(
            @RequestHeader(name = "session_id") Long sessionId, @RequestHeader(name = "student_id") Long userId) {

        try {
            User u = authService.getUserById(userId);
            sessionService.addStudentToSession(u, sessionId);
            return GenericResponse.success("add student to session successfully");
        } catch (Exception e) {
            return GenericResponse.errorOfException(e);
        }
    }

    @GetMapping("remove_student_from_attendance")
    public ResponseEntity<GenericResponse<Object>> removeStudentFromAttendance(
            @RequestHeader(name = "session_id") Long sessionId, @RequestHeader(name = "student_id") Long userId) {

        try {
            User u = authService.getUserById(userId);
            sessionService.removeStudentToSession(u, sessionId);
            return GenericResponse.success("remove student from session successfully");
        } catch (Exception e) {
            return GenericResponse.errorOfException(e);
        }
    }

    @PostMapping("add_object")
    public ResponseEntity<GenericResponse<Object>> getSessionDetails(
            @RequestBody @Valid ObjectCreationRequest objectCreationRequest) {

        try {
            sessionObjectService.save(objectCreationRequest.getItem(), objectCreationRequest.getType(),
                    objectCreationRequest.getParentId());
            return GenericResponse.successWithMessageOnly("add subItem Successfully");
        } catch (Exception e) {
            return GenericResponse.errorOfException(e);
        }
    }

    @PostMapping("create")
    public ResponseEntity<GenericResponse<Object>> createSession(
            @RequestBody @Valid SessionCreationRequest sessionCreationRequest) {
        try {
            Session session = new Session();
            Course course = courseService.findOne(sessionCreationRequest.getCourseId());
            session.setCourse(course);
            session.setStartDate(Instant.parse(sessionCreationRequest.getFromDate()));
            session.setEndDate(Instant.parse(sessionCreationRequest.getToDate()));
            sessionService.save(session);
            return GenericResponse.success("session add successfully");
        } catch (Exception e) {
            return GenericResponse.errorOfException(e);
        }
    }
}
