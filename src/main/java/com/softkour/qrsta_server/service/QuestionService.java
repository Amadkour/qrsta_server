package com.softkour.qrsta_server.service;

import com.softkour.qrsta_server.entity.quiz.Question;
import com.softkour.qrsta_server.repo.quiz.QuestionRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Question save(Question question) {
        return questionRepository.save(question);
    }

    public Page<Question> findAll(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Optional<Question> findOne(Long id) {
        return questionRepository.findById(id);
    }

    public List<Question> findBySession(Long id) {
        return questionRepository.findByCoveredSessions_sessions_id(id);
    }

    public List<Question> findByCourse(Long id) {
        return questionRepository.findByCoveredSessions_course_id(id);
    }

    public void delete(Long id) {
        questionRepository.deleteById(id);
    }

    public List<Question> findByIds(List<Long> longStream) {
        return questionRepository.findAllById(longStream);
    }
}
