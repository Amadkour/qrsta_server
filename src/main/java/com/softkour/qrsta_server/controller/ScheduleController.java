package com.softkour.qrsta_server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.payload.response.StudentSchedualResponse;
import com.softkour.qrsta_server.service.AuthService;
import com.softkour.qrsta_server.service.StudentScheduleService;

@RestController
@RequestMapping("api/shedule/")
public class ScheduleController {

    @Autowired
    StudentScheduleService scheduleService;
    @Autowired
    AuthService authService;

    @GetMapping("all")
    ResponseEntity<GenericResponse<List<StudentSchedualResponse>>> getShedule(
            @RequestHeader(name = "child_phone", required = false) String childPhone) {
        User u;
        if (childPhone != null) {

            u = authService.getUserByPhoneNumber(childPhone);
            return GenericResponse.success(scheduleService.getUserSchedule(u.getId()).stream()
                    .map(e -> e.toStudentSchedualResponse()).toList());

        } else {
            u = MyUtils.getCurrentUserSession(authService);
            if (u.getType() == UserType.TEACHER) {
                return GenericResponse.success(scheduleService.getTeacherSchedule(u.getId()).stream()
                        .map(e -> e.toStudentSchedualResponse()).toList());
            }

        return GenericResponse.success(scheduleService.getUserSchedule(u.getId()).stream()
                .map(e -> e.toStudentSchedualResponse()).toList());

    }

}
}
