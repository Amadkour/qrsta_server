package com.softkour.qrsta_server.service;

import com.softkour.qrsta_server.entity.Session;
import com.softkour.qrsta_server.repo.SessionRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link com.softkour.qrsta_server.domain.Session}.
 */
@Service
@Transactional
public class SessionService {

    private final Logger log = LoggerFactory.getLogger(SessionService.class);

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    /**
     * Save a session.
     *
     * @param session the entity to save.
     * @return the persisted entity.
     */
    public Session save(Session session) {
        log.debug("Request to save Session : {}", session);
        return sessionRepository.save(session);
    }

    /**
     * Update a session.
     *
     * @param session the entity to save.
     * @return the persisted entity.
     */
    public Session update(Session session) {
        log.debug("Request to update Session : {}", session);
        return sessionRepository.save(session);
    }

    /**
     * Partially update a session.
     *
     * @param session the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Session> partialUpdate(Session session) {
        log.debug("Request to partially update Session : {}", session);

        return sessionRepository
                .findById(session.getId())
                .map(existingSession -> {
                    if (session.getCreateAte() != null) {
                        existingSession.setCreateAte(session.getCreateAte());
                    }
                    if (session.getHaveQuiz() != null) {
                        existingSession.setHaveQuiz(session.getHaveQuiz());
                    }

                    return existingSession;
                })
                .map(sessionRepository::save);
    }

    /**
     * Get all the sessions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Session> findAll() {
        log.debug("Request to get all Sessions");
        return sessionRepository.findAll();
    }

    /**
     * Get one session by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Session> findOne(Long id) {
        log.debug("Request to get Session : {}", id);
        return sessionRepository.findById(id);
    }

    /**
     * Delete the session by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Session : {}", id);
        sessionRepository.deleteById(id);
    }
}
