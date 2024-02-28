package com.softkour.qrsta_server.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.course.StudentCourse;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    List<StudentCourse> findByStudent_idAndActiveTrue(Long userId);

    List<StudentCourse> findAllByCourse_teacher_idAndFinishedFalse(Long teacherId);

    double getCourseCostByCourse_teacher_idAndFinishedFalseAndLate(Long teacherId, int late);

    List<StudentCourse> getStudentsByCourse_teacher_idAndStudent_parent_idAndActiveAndFinished(
            Long teacherId, Long parentId, boolean active, boolean finished);

    public List<StudentCourse> getStudentByCourse_teacher_idAndLate(Long teacherId, int late);

    public List<StudentCourse> getStudentByCourse_teacher_idAndLateGreaterThan(Long teacherId, int late);

    public List<StudentCourse> getStudentByCourse_teacher_id(Long teacherId);

}
