package com.softkour.qrsta_server.service;

import com.softkour.qrsta_server.entity.Session;
import com.softkour.qrsta_server.repo.SessionRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SessionService {

    private final Logger log = LoggerFactory.getLogger(SessionService.class);

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session save(Session session) {
        return sessionRepository.save(session);
    }

    @Transactional(readOnly = true)
    public List<Session> findSessionsOfCourse(Long courseId) {
        log.debug("Request to get all Sessions");
        return sessionRepository.findAllByCourse_Id(courseId);
    }

    @Transactional(readOnly = true)
    public Optional<Session> findOne(Long id) {
        log.debug("Request to get Session : {}", id);
        return sessionRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete Session : {}", id);
        sessionRepository.deleteById(id);
    }
}
