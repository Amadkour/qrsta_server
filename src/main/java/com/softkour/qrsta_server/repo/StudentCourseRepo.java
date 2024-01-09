package com.softkour.qrsta_server.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softkour.qrsta_server.entity.StudentCourse;

public interface StudentCourseRepo extends JpaRepository<StudentCourse, Long> {

}
