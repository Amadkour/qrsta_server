package com.softkour.qrsta_server.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softkour.qrsta_server.entity.assiociation_entity.StudentCourse;
import com.softkour.qrsta_server.entity.embedded_pk.StudentCoursePK;

public interface StudentCourseRepo extends JpaRepository<StudentCourse, StudentCoursePK> {

    public List<StudentCourse> findAllByCourse_id(long courseId);

}
