package com.softkour.qrsta_server.repo.course;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softkour.qrsta_server.entity.course.GroupAssignment;

public interface GroupAssignmentRepo extends JpaRepository<GroupAssignment, Long> {

    Optional<List<GroupAssignment>> findAllByAssignment_id(Long id);

    Optional<List<GroupAssignment>> findAllByAssignment_idAndStudents_id(Long id, Long user_id);

}
