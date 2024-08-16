package com.softkour.qrsta_server.repo.quiz;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;

import com.softkour.qrsta_server.entity.quiz.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCoveredSessions_sessions_id(Long sessionId);
    List<Question> findByCoveredSessions_course_id(Long course);
}
