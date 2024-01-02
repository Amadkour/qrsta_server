package com.softkour.qrsta_server.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.repo.UserRepository;
import com.softkour.qrsta_server.security.JwtRequestFilter;
import com.softkour.qrsta_server.payload.mapper.CourseMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.entity.Course;
import com.softkour.qrsta_server.entity.Schedule;
import com.softkour.qrsta_server.payload.request.CourseCreationRequest;
import com.softkour.qrsta_server.payload.request.ScheduleRequest;
import com.softkour.qrsta_server.service.CourseService;
import com.softkour.qrsta_server.service.ScheduleService;
import org.webjars.NotFoundException;

@RestController
@RequestMapping("/api/course/")
public class courseController {
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    CourseService courseService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    UserRepository userRepository;
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
            course.setTeacher(MyUtils.getCurrentUserSession(userRepository));
            courseService.save(course);
            return GenericResponse.successWithMessageOnly("success add course");
        } catch (Exception e) {
            return GenericResponse.error(e.toString());
        }
    }
    @GetMapping("get_my_courses")
    public ResponseEntity<GenericResponse<Object>> getCourses(){
       try {

            User user = MyUtils.getCurrentUserSession(userRepository);
            List<Course> courseList = new ArrayList<>();
            if (user.getType() == UserType.TEACHER) {
                courseList = courseService.getCourses(user.getId());
            } else {
                courseList = user.getCourses().stream().toList();
            }
            return GenericResponse.success(courseList.stream().map(new CourseMapper()::toDTOs));
        }catch (Exception e){
           return GenericResponse.error(e.getMessage());
       }
    }
    @GetMapping("add_my_to_course")
    public ResponseEntity<GenericResponse<Object>> takeCurrentUserInAttendance(@RequestHeader(name = "course_id") Long courseId) {
        try {
            User u = MyUtils.getCurrentUserSession(userRepository);
            courseService.addStudentToCourse(u, courseId);
            return GenericResponse.successWithMessageOnly("add you successfully");
        } catch (Exception e) {
            return GenericResponse.error(e.getMessage());
        }
    }
    @GetMapping("add_student_to_course")
    public ResponseEntity<GenericResponse<Object>> addStudentToAttendance(@RequestHeader(name = "course_id") Long courseId, @RequestHeader(name = "student_id") Long userId) {

        try {
            User u = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("student not found id: ".concat(String.valueOf(userId))));
            courseService.addStudentToCourse(u, courseId);
            return GenericResponse.success("add student to course successfully");
        } catch (Exception e) {
            return GenericResponse.error(e.getMessage());
        }
    }

    @GetMapping("remove_student_from_course")
    public ResponseEntity<GenericResponse<Object>> removeStudentFromAttendance(@RequestHeader(name = "course_id") Long courseId, @RequestHeader(name = "student_id") Long userId) {

        try {
            User u = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("student not found id: ".concat(String.valueOf(userId))));
            courseService.removeStudentFromCourse(u, courseId);
            return GenericResponse.success("remove student from session successfully");
        } catch (Exception e) {
            return GenericResponse.error(e.getMessage());
        }
    }
}
