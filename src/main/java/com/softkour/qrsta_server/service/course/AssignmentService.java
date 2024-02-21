package com.softkour.qrsta_server.service.course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softkour.qrsta_server.entity.course.Assignment;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.repo.course.AssignmentRepo;

@Service
public class AssignmentService {

    @Autowired
    AssignmentRepo assignmentRepo;

    public Assignment save(Assignment assignment) {
        return assignmentRepo.save(assignment);
    }

    public Assignment findById(Long id) {
        return assignmentRepo.findById(id)
                .orElseThrow(() -> new ClientException("assignment", "assignment not found"));
    }

    public List<Assignment> findByCourseId(Long id) {
        return assignmentRepo.findAllByCourseId(id);
    }
}
