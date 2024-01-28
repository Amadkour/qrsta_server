package com.softkour.qrsta_server.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.quiz.StudentCourse;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    StudentCourse findByStudent_id(Long userId);

    List<StudentCourse> findAllByCourse_teacher_idAndFinished(Long teacherId, boolean finished);

    double getCourseCostByCourse_teacher_idAndFinishedAndLate(Long teacherId, boolean finished, int late);

    List<StudentCourse> getStudentsByCourse_teacher_idAndStudent_parent_idAndActiveAndFinished(
            Long teacherId, Long parentId, boolean active, boolean finished);
}
