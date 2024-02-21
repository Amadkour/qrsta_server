package com.softkour.qrsta_server.repo.course;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.softkour.qrsta_server.entity.course.Assignment;

public interface AssignmentRepo extends JpaRepository<Assignment, Long> {

    List<Assignment> findAllByCourseId(Long id);

}
