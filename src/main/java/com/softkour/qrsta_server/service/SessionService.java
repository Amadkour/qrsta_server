package com.softkour.qrsta_server.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import com.softkour.qrsta_server.entity.Session;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.repo.SessionRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class SessionService {

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

    public Session addStudentToSession(User user, Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("session not found id: ".concat(sessionId.toString())));
        session.addStudent(user);
        return sessionRepository.save(session);
    }

    public Session removeStudentToSession(User user, Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("session not found id: ".concat(sessionId.toString())));
        session.removeStudent(user);
        return sessionRepository.save(session);
    }
}
