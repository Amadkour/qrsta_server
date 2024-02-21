package com.softkour.qrsta_server.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.softkour.qrsta_server.entity.public_entity.StudentSchedule;
import com.softkour.qrsta_server.entity.quiz.StudentCourse;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.payload.request.ObjectCreationRequest;
import com.softkour.qrsta_server.payload.request.SessionCreationRequest;
import com.softkour.qrsta_server.payload.response.SessionObjectResponse;
import com.softkour.qrsta_server.repo.StudentScheduleRepo;
import com.softkour.qrsta_server.service.AuthService;
import com.softkour.qrsta_server.service.SessionObjectService;
import com.softkour.qrsta_server.service.SessionService;
import com.softkour.qrsta_server.service.course.CourseService;

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
        StudentScheduleRepo scheduleRepo;
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
                                sessionService.addStudentToSession(u, sessionId)
                                                .toSessionDateAndStudentGradeWithAttendance(true));

        }

        @GetMapping("add_student_to_attendance")
        public ResponseEntity<GenericResponse<Object>> addStudentToAttendance(
                        @RequestHeader(name = "session_id") Long sessionId,
                        @RequestHeader(name = "student_id") List<Long> userIds) {

                Session s = sessionService.addStudentsToSession(authService, userIds, sessionId);
                return GenericResponse
                                .success(s.toSessionDetailsStudent());

        }

        @GetMapping("remove_student_from_attendance")
        public ResponseEntity<GenericResponse<Object>> removeStudentFromAttendance(
                        @RequestHeader(name = "session_id") Long sessionId,
                        @RequestHeader(name = "student_id") Long userId) {
                User u = authService.getUserById(userId);
                sessionService.removeStudentToSession(u, sessionId);
                return GenericResponse.successWithMessageOnly("remove student from session successfully");

        }

        @GetMapping("delete")
        public ResponseEntity<GenericResponse<Object>> delete(
                        @RequestHeader(name = "session_id") Long sessionId) {
                sessionService.delete(sessionId);
                return GenericResponse.successWithMessageOnly("remove student from session successfully");

        }

        @PostMapping("add_object")
        public ResponseEntity<GenericResponse<Object>> addObject(
                        @RequestBody @Valid ObjectCreationRequest objectCreationRequest) {
                SessionObject sessionObject = sessionObjectService.save(objectCreationRequest.getItem(),
                                objectCreationRequest.getType(),
                                objectCreationRequest.getParentId(),
                                sessionService.findOne(objectCreationRequest.getSessionId()));

                return GenericResponse.success(new SessionObjectResponse(sessionObject.getTitle(), null,
                                sessionObject.getType(), sessionObject.getCreatedDate(), sessionObject.getId()));

        }

        @PostMapping("create")
        public ResponseEntity<GenericResponse<Object>> createSession(
                        @RequestBody @Valid SessionCreationRequest sessionCreationRequest) {
                Session session = new Session();
                Course course = courseService.findOne(sessionCreationRequest.getCourseId());
                session.setLabel("session" + sessionService.findSessionsOfCourse(course.getId()).size());
                session.setCourse(course);
                Instant serverTime = Instant.now();
                log.warn(sessionCreationRequest.getCurrentDate());
                Instant clientTime = Instant.parse(sessionCreationRequest.getCurrentDate());
                Long diff = serverTime.toEpochMilli() - clientTime.toEpochMilli();
                log.warn(serverTime.toString());
                log.warn(clientTime.toString());
                log.warn(diff + "");
                if (diff < 0) {
                        session.setStartDate(Instant.parse(sessionCreationRequest.getFromDate()).plusMillis(diff));
                        session.setEndDate(Instant.parse(sessionCreationRequest.getToDate()).plusMillis(diff));
                } else {
                        session.setStartDate(Instant.parse(sessionCreationRequest.getFromDate()).minusMillis(diff));
                        session.setEndDate(Instant.parse(sessionCreationRequest.getToDate()).minusMillis(diff));
                }

                log.warn("start afet maintance:==>" + session.getStartDate());
                log.warn("end afet maintance:==>" + session.getEndDate());
                session = sessionService.save(session);
                /// add it in student schedual
                List<StudentCourse> students = session.getCourse().getStudents().stream().collect(Collectors.toList());
                log.warn(students.stream().map(e -> e.getStudent().getPhoneNumber()).toList() + "");
                for (int i = 0; i < students.size(); i++) {
                        StudentSchedule item = new StudentSchedule();
                        item.setDone(false);
                        item.setRead(false);
                        item.setSession(session);
                        item.setCourse(course);
                        item.setUser(students.get(i).getStudent());
                        scheduleRepo.save(item);

                }
                log.warn(scheduleRepo.count() + "");
                return GenericResponse.success(session.toSessionDateAndStudentGrade(-1L));

        }

        @GetMapping("future_course_sessions")
        public ResponseEntity<GenericResponse<Object>> getFutureCourseSessions(
                        @RequestHeader(name = "course_id") Long courseId) {
                return GenericResponse.success(
                                sessionService.findFutureSessionsOfCourse(courseId, Instant.now()).stream()
                                                .map((e) -> e.toSessionNameAndId())
                                                .toList());

        }

        @GetMapping("old_course_sessions")
        public ResponseEntity<GenericResponse<Object>> getOldCourseSessions(
                        @RequestHeader(name = "course_id") Long courseId) {
                return GenericResponse.success(
                                sessionService.findOldSessionsOfCourse(courseId, Instant.now()).stream()
                                                .map((e) -> e.toSessionNameAndId())
                                                .toList());

        }
}
