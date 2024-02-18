package com.softkour.qrsta_server.repo;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.public_entity.StudentSchedual;

@Repository
public interface StudentScheduleRepo extends JpaRepository<StudentSchedual, Long> {

    List<StudentSchedual> getScheduleByUser_idAndCreatedDateAfter(Long studentId, Instant Date);

}
