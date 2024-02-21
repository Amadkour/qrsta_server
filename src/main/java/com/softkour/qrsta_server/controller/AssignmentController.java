package com.softkour.qrsta_server.controller;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.entity.course.Assignment;
import com.softkour.qrsta_server.entity.course.GroupAssignment;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.payload.response.AssignmentResponse;
import com.softkour.qrsta_server.payload.response.GroupAssignmentResponse;
import com.softkour.qrsta_server.service.AuthService;
import com.softkour.qrsta_server.service.course.AssignmentService;
import com.softkour.qrsta_server.service.course.CourseService;
import com.softkour.qrsta_server.service.course.GroupAssignmentService;

@RestController
@RequestMapping("/api/assignment/")
public class AssignmentController {
    @Autowired
    GroupAssignmentService groupAssignmentService;
    @Autowired
    AssignmentService assignmentService;
    @Autowired
    AuthService authService;
    @Autowired
    CourseService courseService;

    @GetMapping("creat")
    public void create(
            @RequestHeader("due_date") String dueString,
            @RequestHeader("course_id") Long courseId,
            @RequestHeader("max_count") int maxCount,
            @RequestHeader("min_count") int minCount,
            @RequestHeader("title") String title,
            @RequestHeader("description") String description) {

        Assignment assignment = new Assignment();
        assignment.setDueDate(Instant.parse(dueString));
        assignment.setMax_count(maxCount);
        assignment.setMin_count(minCount);
        assignment.setTitle(title);
        assignment.setCourse(courseService.findOne(courseId));
        assignment.setDescription(description);
        assignmentService.save(assignment);

    }

    @GetMapping("update")
    public void update(
            @RequestHeader(name = "id") Long id,
            @RequestHeader(name = "due_date", required = false) String dueString,
            @RequestHeader(name = "max_count", required = false) Integer maxCount,
            @RequestHeader(name = "min_count", required = false) Integer minCount,
            @RequestHeader(name = "title", required = false) String title,
            @RequestHeader(name = "description", required = false) String description) {

        Assignment assignment = assignmentService.findById(id);
        if (dueString != null)
            assignment.setDueDate(Instant.parse(dueString));

        if (maxCount != null)
            assignment.setMax_count(maxCount);

        if (minCount != null)
            assignment.setMin_count(minCount);

        if (title != null)
            assignment.setTitle(title);

        if (description != null)
            assignment.setDescription(description);
        assignmentService.save(assignment);
    }

    @GetMapping("course_assignments")
    public ResponseEntity<GenericResponse<Stream<AssignmentResponse>>> courseAssignments(
            @RequestHeader(name = "course_id") Long id) {

        List<Assignment> assignments = assignmentService.findByCourseId(id);

        return GenericResponse.success(assignments.stream().map(e -> e.toAssignmentResponse()));
    }

    @GetMapping("assignment")
    public ResponseEntity<GenericResponse<AssignmentResponse>> getAssignments(
            @RequestHeader(name = "id") Long id) {

        Assignment assignment = assignmentService.findById(id);

        return GenericResponse.success(assignment.toAssignmentResponse());
    }

    @GetMapping("accept")
    public ResponseEntity<GenericResponse<GroupAssignmentResponse>> accept(
            @RequestHeader(name = "id") Long id) {

        GroupAssignment group = groupAssignmentService.acceptGroup(id);

        return GenericResponse.success(group.toGroupResponse());
    }

    @GetMapping("add_group")
    public ResponseEntity<GenericResponse<AssignmentResponse>> addGroup(
            @RequestHeader(name = "assignment_id") Long assignmentId,
            @RequestHeader(name = "students") List<Long> studentIds) {

        Assignment assignment = assignmentService.findById(assignmentId);
        if (assignment.getMax_count() < studentIds.size())
            throw new ClientException("group", "group excedded max count");
        if (assignment.getMin_count() < studentIds.size())
            throw new ClientException("group", "group must be more than or equal" + assignment.getMin_count());
        GroupAssignment group = new GroupAssignment();
        group.setStudents(studentIds.stream().map(e -> authService.getUserById(e)).collect(Collectors.toSet()));
        group.setAssignment(assignment);
        groupAssignmentService.save(group);
        return GenericResponse.success(assignment.toAssignmentResponse());
    }

    @GetMapping("add_student")
    public ResponseEntity<GenericResponse<GroupAssignmentResponse>> addStudent(
            @RequestHeader(name = "assignment_id") Long assignmentId,
            @RequestHeader(name = "group_id") Long groupId,
            @RequestHeader(name = "studrnt_id") Long studentId) {
        Assignment assignment = assignmentService.findById(assignmentId);
        GroupAssignment group = groupAssignmentService.findById(groupId);
        if (assignment.getMax_count() == group.getStudents().size())
            throw new ClientException("group", "group excedded max count");
        group.addStudent(authService.getUserById(studentId));
        return GenericResponse.success(group.toGroupResponse());
    }

}
