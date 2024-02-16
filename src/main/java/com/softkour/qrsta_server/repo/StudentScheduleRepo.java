package com.softkour.qrsta_server.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.softkour.qrsta_server.entity.public_entity.StudentSchedual;

public interface StudentScheduleRepo extends JpaRepository<StudentSchedual, Long> {

    List<StudentSchedual> getScheduleByUser_id(Long studentId);

}
