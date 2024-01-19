package com.softkour.qrsta_server.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.StudentCourse;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    StudentCourse findByStudent_id(Long userId);

    List<StudentCourse> findAllByCourseTeacherIdAndFinished(Long teacherId, boolean finished);

    double getCourseCostByCourseTeacherIdAndFinishedAndLate(Long teacherId, boolean finished, int late);

    List<StudentCourse> getStudentsByCourseTeacherIdAndStudentParentIdAndActiveAndFinished(
            Long teacherId, Long parentId, boolean active, boolean finished);
}
