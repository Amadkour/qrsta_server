package com.softkour.qrsta_server.controller;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.entity.Course;
import com.softkour.qrsta_server.entity.Schedule;
import com.softkour.qrsta_server.request.CourseCreationRequest;
import com.softkour.qrsta_server.request.ScheduleRequest;
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
                schedule.setDat(scheduleRequest.getDay());
                savedSchedules.add(scheduleService.save(schedule));
            }
            // ===========[ schedule ]===============//
            Course course = new Course();
            course.setName(request.getName());
            course.setType(request.getCourseType());
            course.setCost(request.getCost());
            logger.warn("in progress:2");

            course.setSchedules(savedSchedules);
            logger.warn("in progress:3");

            courseService.save(course);
            logger.warn("save schedule success");

            return GenericResponse.successWithMessageOnly("success add course");
        } catch (Exception e) {
            return GenericResponse.error(e.toString());
        }
    }
}
