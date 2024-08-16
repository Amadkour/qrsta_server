package com.softkour.qrsta_server.repo.quiz;

import com.softkour.qrsta_server.entity.quiz.Option;
import com.softkour.qrsta_server.entity.quiz.StudentQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentQuizRepo extends JpaRepository<StudentQuiz, Long> {

    List<StudentQuiz> findByStudent_idAndCourse_quiz_id(Long studentId, Long quizId);
    StudentQuiz findByStudent_id(Long studentId);
    StudentQuiz findByCourse_quiz_id(Long quizId);


}
