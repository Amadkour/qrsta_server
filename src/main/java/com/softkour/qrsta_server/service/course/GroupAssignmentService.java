package com.softkour.qrsta_server.service.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.softkour.qrsta_server.entity.course.GroupAssignment;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.repo.UserRepository;
import com.softkour.qrsta_server.repo.course.GroupAssignmentRepo;

@Service
public class GroupAssignmentService {

    @Autowired
    GroupAssignmentRepo assignmentRepo;
    @Autowired
    UserRepository userRepository;

    public GroupAssignment save(GroupAssignment assignment) {
        return assignmentRepo.save(assignment);
    }

    public GroupAssignment findById(Long id) {
        return assignmentRepo.findById(id)
                .orElseThrow(() -> new ClientException("assignment_group", "assignment group not found"));
    }

    public GroupAssignment addStudent(Long id, Long studentId) {
        User u = userRepository.findById(studentId)
                .orElseThrow(() -> new ClientException("assignment_group", "student not found"));
        GroupAssignment group = findById(id);
        group.addStudent(u);
        return assignmentRepo.save(group);
    }

    public void deleteGroup(Long id) {
        assignmentRepo.deleteById(id);
    }

    public GroupAssignment acceptGroup(Long id) {
        GroupAssignment group = assignmentRepo.findById(id)
                .orElseThrow(() -> new ClientException("assignment_group", "assignment group not found"));
        group.setActive(true);
        return assignmentRepo.save(group);

    }
}
