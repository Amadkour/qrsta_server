package com.softkour.qrsta_server.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.Course;

import java.util.List;

/**
 * Spring Data JPA repository for the Course entity.
 */
// @SuppressWarnings("unused")
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> getCourseByTeacherId(Long teacherId);
}
