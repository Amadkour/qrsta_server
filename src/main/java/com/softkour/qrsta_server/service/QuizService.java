package com.softkour.qrsta_server.service;

import com.softkour.qrsta_server.entity.Quiz;
import com.softkour.qrsta_server.repo.QuizRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.softkour.qrsta_server.domain.Quiz}.
 */
@Service
@Transactional
public class QuizService {

    private final Logger log = LoggerFactory.getLogger(QuizService.class);

    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    /**
     * Save a quiz.
     *
     * @param quiz the entity to save.
     * @return the persisted entity.
     */
    public Quiz save(Quiz quiz) {
        log.debug("Request to save Quiz : {}", quiz);
        return quizRepository.save(quiz);
    }

    /**
     * Update a quiz.
     *
     * @param quiz the entity to save.
     * @return the persisted entity.
     */
    public Quiz update(Quiz quiz) {
        log.debug("Request to update Quiz : {}", quiz);
        return quizRepository.save(quiz);
    }

    /**
     * Partially update a quiz.
     *
     * @param quiz the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Quiz> partialUpdate(Quiz quiz) {
        log.debug("Request to partially update Quiz : {}", quiz);

        return quizRepository
                .findById(quiz.getId())
                .map(existingQuiz -> {
                    if (quiz.getQuizId() != null) {
                        existingQuiz.setQuizId(quiz.getQuizId());
                    }
                    if (quiz.getCreateAt() != null) {
                        existingQuiz.setCreateAt(quiz.getCreateAt());
                    }
                    if (quiz.getStartDate() != null) {
                        existingQuiz.setStartDate(quiz.getStartDate());
                    }
                    if (quiz.getQuestionsPerStudent() != null) {
                        existingQuiz.setQuestionsPerStudent(quiz.getQuestionsPerStudent());
                    }
                    if (quiz.getType() != null) {
                        existingQuiz.setType(quiz.getType());
                    }

                    return existingQuiz;
                })
                .map(quizRepository::save);
    }

    /**
     * Get all the quizzes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Quiz> findAll() {
        log.debug("Request to get all Quizzes");
        return quizRepository.findAll();
    }

    /**
     * Get all the quizzes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Quiz> findAllWithEagerRelationships(Pageable pageable) {
        return quizRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one quiz by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Quiz> findOne(Long id) {
        log.debug("Request to get Quiz : {}", id);
        return quizRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the quiz by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Quiz : {}", id);
        quizRepository.deleteById(id);
    }
}
