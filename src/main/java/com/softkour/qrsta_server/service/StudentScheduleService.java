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
        return scheduleRepo.getScheduleByUser_idAndCreatedDateAfter(userId, Instant.now().minus(3, ChronoUnit.DAYS));
    }

}
