package com.softkour.qrsta_server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softkour.qrsta_server.entity.course.Session;
import com.softkour.qrsta_server.entity.user.Student;
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

    public Session addStudentToSession(Student user, Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(
                        () -> new ClientException("session", "session not found id: ".concat(sessionId.toString())));
        /// check user join this course
        log.warn("=================student courses================");
        log.warn(user.getCourses().stream().map(e -> e.getCourse().getId()).toList().toString());
        log.warn(session.getCourse().getId().toString());
        log.warn("============================================");

        if (user.getCourses().stream().anyMatch(c -> c.getCourse().getId() == session.getCourse().getId())) {
            session.addStudent(user);
            log.warn(session.getStudents().stream().map(e -> e.getId()).toList().toString());
            return sessionRepository.save(session);
        } else {
            throw new ClientException("student",
                    "this student not joint to this course: user id=".concat(user.getId().toString()));
        }
    }

    public Session removeStudentToSession(Student user, Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(
                        () -> new ClientException("session", "session not found id: ".concat(sessionId.toString())));
        session.removeStudent(user);
        return sessionRepository.save(session);
    }
}
