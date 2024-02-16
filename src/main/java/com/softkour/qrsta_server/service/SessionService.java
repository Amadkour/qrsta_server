package com.softkour.qrsta_server.service;

import java.time.Instant;
import java.util.List;

import org.apache.logging.log4j.CloseableThreadContext.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.course.Session;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.repo.SessionRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class SessionService {

    @Autowired
    CourseService courseService;

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session save(Session session) {
        return sessionRepository.save(session);
    }

    @Transactional(readOnly = true)
    public List<Session> findSessionsOfCourse(Long courseId) {
        return sessionRepository.findAllByCourse_Id(courseId);
    }

    @Transactional(readOnly = true)
    public List<Session> findFutureSessionsOfCourse(Long courseId, Instant date) {
        return sessionRepository.findAllByCourse_IdAndStartDateAfter(courseId, date);
    }

    @Transactional(readOnly = true)
    public List<Session> findOldSessionsOfCourse(Long courseId, Instant date) {
        return sessionRepository.findAllByCourse_IdAndStartDateBefore(courseId, date);
    }

    // @Transactional(readOnly = true)
    public Session findOne(long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ClientException("session_id", "Session not Found id: " + id));
    }

    public Session getReferenceById(long id) {
        return sessionRepository.getReferenceById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete Session : {}", id);
        sessionRepository.deleteById(id);
    }

    public Session addStudentToSession(User user, Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(
                        () -> new ClientException("session", "session not found id: ".concat(sessionId.toString())));
        if (user.getCourses().stream().anyMatch(c -> c.getCourse().getId() == session.getCourse().getId())) {
            session.addStudent(user);
            return sessionRepository.save(session);
        } else {
            throw new ClientException("student",
                    "this student not joint to this course: user id=".concat(user.getId().toString()));
        }
    }

    public Session addStudentsToSession(AuthService authService, List<Long> userIds, Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(
                        () -> new ClientException("session", "session not found id: ".concat(sessionId.toString())));
        for (int i = 0; i < userIds.size(); i++) {
            User user = authService.getUserById(userIds.get(i));
            /// check user join this course
        if (user.getCourses().stream().anyMatch(c -> c.getCourse().getId() == session.getCourse().getId())) {
            session.addStudent(user);
            sessionRepository.save(session);
        } else {
            throw new ClientException("student",
                    "this student not joint to this course: user id=".concat(user.getId().toString()));
        }
    }
    return sessionRepository.findById(sessionId)
            .orElseThrow(
                    () -> new ClientException("session", "session not found id: ".concat(sessionId.toString())));
}

    public Session removeStudentToSession(User user, Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(
                        () -> new ClientException("session", "session not found id: ".concat(sessionId.toString())));
        session.removeStudent(user);
        return sessionRepository.save(session);
    }
}
