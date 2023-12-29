package com.softkour.qrsta_server.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

import com.softkour.qrsta_server.entity.Quiz;

public interface QuizRepositoryWithBagRelationships {
    Optional<Quiz> fetchBagRelationships(Optional<Quiz> quiz);

    List<Quiz> fetchBagRelationships(List<Quiz> quizzes);

    Page<Quiz> fetchBagRelationships(Page<Quiz> quizzes);
}
