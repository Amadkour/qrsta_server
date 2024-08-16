package com.softkour.qrsta_server.repo.quiz;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.quiz.Quiz;
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    List<Quiz> findAllByCourses_course_teacher_id(Long teacherId);

    List<Quiz> findAllByCourses_students_student_id(Long studentId);

    Quiz findByIdAndCourses_course_name(Long quizId,String courseName);
}
