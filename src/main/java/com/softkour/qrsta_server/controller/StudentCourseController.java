package com.softkour.qrsta_server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.entity.StudentCourse;
import com.softkour.qrsta_server.repo.StudentCourseRepo;

@RestController
@RequestMapping("/late/")
public class StudentCourseController {
    @Autowired
    StudentCourseRepo studentCourseRepo;

    @GetMapping("all")
    public ResponseEntity<GenericResponse<List<String>>> getAll() {
        return GenericResponse
                .success(studentCourseRepo.findAll().stream()
                        .map((e) -> e.getStudent().getName().concat(" ").concat(e.getCourse().getName())).toList());
    }
}
