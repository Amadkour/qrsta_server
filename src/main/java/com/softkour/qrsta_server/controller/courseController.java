package com.softkour.qrsta_server.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.Course;
import com.softkour.qrsta_server.entity.Schedule;
import com.softkour.qrsta_server.entity.StudentCourse;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.payload.request.CourseCreationRequest;
import com.softkour.qrsta_server.payload.request.ScheduleRequest;
import com.softkour.qrsta_server.service.AuthService;
import com.softkour.qrsta_server.service.CourseService;
import com.softkour.qrsta_server.service.ScheduleService;

@RestController
@RequestMapping("/api/course/")
public class courseController {
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    CourseService courseService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    AuthService authService;

    @PostMapping("add_course")
    public ResponseEntity<GenericResponse<Object>> addCourse(@RequestBody CourseCreationRequest request) {
        try {
            // ===========[ schedule ]===============//
            Set<Schedule> savedSchedules = new HashSet<>();
            for (int i = 0; i < request.getSchedules().size(); i++) {

                ScheduleRequest scheduleRequest = request.getSchedules().get(i);
                Schedule schedule = new Schedule();
                schedule.setFromTime(scheduleRequest.getFrom());
                schedule.setToTime(scheduleRequest.getTo());
                schedule.setDay(scheduleRequest.getDay());
                savedSchedules.add(scheduleService.save(schedule));
            }
            // ===========[ schedule ]===============//
            Course course = new Course();
            course.setName(request.getName());
            course.setType(request.getCourseType());
            course.setCost(request.getCost());
            course.setSchedules(savedSchedules);
            course.setTeacher(MyUtils.getCurrentUserSession(authService));
            courseService.save(course);
            return GenericResponse.successWithMessageOnly("success add course");
        } catch (Exception e) {
            return GenericResponse.errorOfException(e);
        }
    }

    @GetMapping("get_course_details")
    public ResponseEntity<GenericResponse<Object>> addCourseDetails(@RequestHeader Long courseId) {

        return GenericResponse
                .success(courseService.findOne(courseId).toSessionDetailsStudent());

    }

    @GetMapping("get_my_courses")
    public ResponseEntity<GenericResponse<Object>> getCourses() {
        try {

            User user = MyUtils.getCurrentUserSession(authService);
            if (user.getType() == UserType.TEACHER) {
                List<Course> courseList = courseService.getCourses(user.getId());
                return GenericResponse.success(courseList.stream().map((e) -> e.toCourseResponse()));

            } else {
                List<StudentCourse> courseList = user.getCourses().stream().toList();
                return GenericResponse.success(courseList.stream().map((e) -> e.getCourse().toCourseResponse()));

            }
        } catch (Exception e) {
            return GenericResponse.errorOfException(e);
        }
    }

    @GetMapping("add_my_to_course")
    public ResponseEntity<GenericResponse<Object>> takeCurrentUserInAttendance(
            @RequestHeader(name = "course_id") Long courseId) {
        try {
            User u = MyUtils.getCurrentUserSession(authService);
            courseService.addStudentToCourse(u, courseId);
            return GenericResponse.successWithMessageOnly("add you successfully");
        } catch (Exception e) {
            return GenericResponse.errorOfException(e);
        }
    }

    @GetMapping("add_student_to_course")
    public ResponseEntity<GenericResponse<Object>> addStudentToAttendance(
            @RequestHeader(name = "course_id") Long courseId, @RequestHeader(name = "student_id") Long userId) {

        try {
            User u = authService.getUserById(userId);
            courseService.addStudentToCourse(u, courseId);
            return GenericResponse.success("add student to course successfully");
        } catch (Exception e) {
            return GenericResponse.errorOfException(e);
        }
    }

    @GetMapping("remove_student_from_course")
    public ResponseEntity<GenericResponse<Object>> removeStudentFromAttendance(
            @RequestHeader(name = "course_id") Long courseId, @RequestHeader(name = "student_id") Long userId) {

        try {
            User u = authService.getUserById(userId);
            courseService.removeStudentFromCourse(u, courseId);
            return GenericResponse.success("remove student from session successfully");
        } catch (Exception e) {
            return GenericResponse.errorOfException(e);
        }
    }
}
