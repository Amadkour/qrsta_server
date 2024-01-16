package com.softkour.qrsta_server.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> getCourseByTeacherId(Long teacherId);

    List<Course> getCourseByTeacherIdAndStudents_active(Long teacherId, boolean active);
}
