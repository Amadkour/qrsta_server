package com.softkour.qrsta_server.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softkour.qrsta_server.entity.public_entity.StudentSchedule;
import com.softkour.qrsta_server.payload.response.StudentSchedualResponse;
import com.softkour.qrsta_server.repo.StudentScheduleRepo;

@Service
public class StudentScheduleService {
    @Autowired
    StudentScheduleRepo scheduleRepo;

    public List<StudentSchedule> getUserSchedule(Long userId) {
        return scheduleRepo.getScheduleByUser_idAndCreatedDateAfterOrderByCreatedDateDesc(userId,
                Instant.now().minus(3, ChronoUnit.DAYS));
    }

    public List<StudentSchedule> getTeacherSchedule(Long userId) {
        return scheduleRepo.getScheduleByCourse_teacher_idAndQuestionNotNullOrderByCreatedDateDesc(userId);
    }

    public boolean correct(String scheduleId, String correctAnswer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'correct'");
    }

}
