package com.softkour.qrsta_server.service;

import com.softkour.qrsta_server.entity.quiz.Question;
import com.softkour.qrsta_server.repo.QuestionRepository;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuestionService {


    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question save(Question question) {
        return questionRepository.save(question);
    }
    public Question update(Question question) {
        return questionRepository.save(question);
    }

    @Transactional(readOnly = true)
    public Page<Question> findAll(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Question> findOne(Long id) {
        return questionRepository.findOneWithEagerRelationships(id);
    }

    @Transactional(readOnly = true)
    public List<Question> findBySession(Long id) {
        return questionRepository.findByCoveredSessions_sessions_session_id(id);
    }

    public void delete(Long id) {
        questionRepository.deleteById(id);
    }
}
