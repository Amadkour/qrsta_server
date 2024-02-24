package com.softkour.qrsta_server.repo;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.public_entity.StudentSchedule;

@Repository
public interface StudentScheduleRepo extends JpaRepository<StudentSchedule, Long> {

    List<StudentSchedule> getScheduleByUser_idAndCreatedDateAfterOrderByCreatedDateDesc(Long studentId, Instant Date);

    List<StudentSchedule> getScheduleByCourse_teacher_idAndQuestionNotNullOrderByCreatedDateDesc(Long teacherId);

}
