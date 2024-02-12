package com.softkour.qrsta_server.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.softkour.qrsta_server.entity.course.Course;
import com.softkour.qrsta_server.entity.course.Session;
import com.softkour.qrsta_server.entity.course.SessionObject;
import com.softkour.qrsta_server.entity.post.Post;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.payload.request.ObjectCreationRequest;
import com.softkour.qrsta_server.payload.request.SessionCreationRequest;
import com.softkour.qrsta_server.payload.response.SessionAndSocialResponce;
import com.softkour.qrsta_server.payload.response.SessionDetailsStudent;
import com.softkour.qrsta_server.payload.response.SessionObjectResponse;
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
    CourseService courseService;
    @Autowired
    AuthService authService;
    @Autowired
    SessionObjectService sessionObjectService;

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

    @GetMapping("student_session_attendance")
    public ResponseEntity<GenericResponse<Object>> getStudentSessionAttendance(
            @RequestHeader(name = "child_id") Long childId,
            @RequestHeader(name = "course_id") Long courseId) {
        Course course = courseService.findOne(courseId);
        Set<Session> sessions = course.getSessions();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("attendance", sessions.stream()
                .map(s -> s.getStudents().stream()
                        .anyMatch(b -> b.getId() == childId))
                .toList());
        map.put("date", sessions.stream().map(e -> e.getStartDate()).toList());
        return GenericResponse.success(map);

    }

    @GetMapping("session_details")
    public ResponseEntity<GenericResponse<Object>> getSessionDetails(
            @RequestHeader(name = "session_id") Long sessionId) {
        Session session = sessionService.findOne(sessionId);
        return GenericResponse.success(session.toSessionDetailsWithoutStudents());

    }

    @GetMapping("take_attendance")
    public ResponseEntity<GenericResponse<Object>> takeCurrentUserInAttendance(
            @RequestHeader(name = "session_id") Long sessionId) {
            User u = MyUtils.getCurrentUserSession(authService);
            ;
            return GenericResponse.success(
                    sessionService.addStudentToSession(u, sessionId).toSessionDateAndStudentGradeWithAttendance(true));

    }

    @GetMapping("add_student_to_attendance")
    public ResponseEntity<GenericResponse<Object>> addStudentToAttendance(
            @RequestHeader(name = "session_id") Long sessionId, @RequestHeader(name = "student_id") Long userId) {

        try {
            User u = authService.getUserById(userId);
            Session s = sessionService.addStudentToSession(u, sessionId);
            return GenericResponse
                    .success(s.toSessionDetailsStudent().getStudents().stream().filter(e -> e.getId() == userId)
                            .findFirst().orElseThrow(
                                    () -> new ClientException("student",
                                            "student not found id: ".concat(String.valueOf(userId)))));
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
    public ResponseEntity<GenericResponse<Object>> addObject(
            @RequestBody @Valid ObjectCreationRequest objectCreationRequest) {
        SessionObject sessionObject = sessionObjectService.save(objectCreationRequest.getItem(),
                objectCreationRequest.getType(),
                objectCreationRequest.getParentId(), sessionService.findOne(objectCreationRequest.getSessionId()));
        return GenericResponse.success(new SessionObjectResponse(sessionObject.getTitle(), null,
                sessionObject.getType(), sessionObject.getCreatedDate(), sessionObject.getId()));

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
            session = sessionService.save(session);
            return GenericResponse.success(session.toSessionDateAndStudentGrade());
        } catch (Exception e) {
            return GenericResponse.errorOfException(e);
        }
    }
}
