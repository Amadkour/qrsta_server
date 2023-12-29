package com.softkour.qrsta_server.service;

import com.softkour.qrsta_server.entity.Question;
import com.softkour.qrsta_server.repo.QuestionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link com.softkour.qrsta_server.domain.Question}.
 */
@Service
@Transactional
public class QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * Save a question.
     *
     * @param question the entity to save.
     * @return the persisted entity.
     */
    public Question save(Question question) {
        log.debug("Request to save Question : {}", question);
        return questionRepository.save(question);
    }

    /**
     * Update a question.
     *
     * @param question the entity to save.
     * @return the persisted entity.
     */
    public Question update(Question question) {
        log.debug("Request to update Question : {}", question);
        return questionRepository.save(question);
    }

    /**
     * Partially update a question.
     *
     * @param question the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Question> partialUpdate(Question question) {
        log.debug("Request to partially update Question : {}", question);

        return questionRepository
                .findById(question.getId())
                .map(existingQuestion -> {
                    if (question.getQuestionId() != null) {
                        existingQuestion.setQuestionId(question.getQuestionId());
                    }
                    if (question.getName() != null) {
                        existingQuestion.setName(question.getName());
                    }
                    if (question.getCreateAt() != null) {
                        existingQuestion.setCreateAt(question.getCreateAt());
                    }
                    if (question.getGrade() != null) {
                        existingQuestion.setGrade(question.getGrade());
                    }

                    return existingQuestion;
                })
                .map(questionRepository::save);
    }

    /**
     * Get all the questions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Question> findAll(Pageable pageable) {
        log.debug("Request to get all Questions");
        return questionRepository.findAll(pageable);
    }

    /**
     * Get all the questions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Question> findAllWithEagerRelationships(Pageable pageable) {
        return questionRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one question by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Question> findOne(Long id) {
        log.debug("Request to get Question : {}", id);
        return questionRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the question by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Question : {}", id);
        questionRepository.deleteById(id);
    }
}
