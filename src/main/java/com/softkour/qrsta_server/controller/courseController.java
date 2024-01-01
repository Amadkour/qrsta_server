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
    @GetMapping("")
    public ResponseEntity<GenericResponse<Object>> getCourses(){
       try {
            String phoneNumber = MyUtils.getUserPhone(JwtRequestFilter.username);
            User user = userRepository.findUserByPhoneNumber(phoneNumber);
            List<Course> courseList = new ArrayList<>();
            if (user.getType() == UserType.TEACHER) {
                courseList = courseService.getCourses(user.getId());
            } else {
                courseList = user.getCourses().stream().toList();
              int a=  courseList.get(0).getStudents().size();
            }
            return GenericResponse.success(courseList.stream().map(CourseMapper.INSTANCE::toDTOs));
        }catch (Exception e){
           return GenericResponse.error(e.getMessage());
       }
    }
}
