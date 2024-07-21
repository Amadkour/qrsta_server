package com.softkour.qrsta_server.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;

import com.softkour.qrsta_server.entity.quiz.Question;

@Repository
public interface QuestionRepository extends QuestionRepositoryWithBagRelationships, JpaRepository<Question, Long> {
    default Optional<Question> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    List<Question> findByCoveredSessions_sessions_session_id(Long sessionId);
}
