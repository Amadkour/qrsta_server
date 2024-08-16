package com.softkour.qrsta_server.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.course.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {


    List<Schedule> findAllByCourses_teacher_Id(Long userId);

    List<Schedule> findAllByCourses_students_student_Id(Long userId);

    void deleteAllByCourses_id(Long courseId);
}
