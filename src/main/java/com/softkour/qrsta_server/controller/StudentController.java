package com.softkour.qrsta_server.controller;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.repo.StudentScheduleRepo;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    StudentScheduleRepo studentScheduleRepo;

    public ResponseEntity<GenericResponse<Stream<Object>>> getSchedule(@RequestHeader("student_id") Long studentId) {

        return GenericResponse.success(
                studentScheduleRepo.getScheduleByUser_id(studentId).stream().map(e -> e.toStudentSchedualResponse()));

    }

}
