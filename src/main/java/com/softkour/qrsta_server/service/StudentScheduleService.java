package com.softkour.qrsta_server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softkour.qrsta_server.payload.response.StudentSchedualResponse;
import com.softkour.qrsta_server.repo.StudentScheduleRepo;

@Service
public class StudentScheduleService {
    @Autowired
    StudentScheduleRepo scheduleRepo;

    public List<StudentSchedualResponse> getUserSchedule(Long userId) {
        return scheduleRepo.getScheduleByUser_idAndCreatedDateAfter(userId, null).stream()
                .map(e -> e.toStudentSchedualResponse()).toList();
    }

}
