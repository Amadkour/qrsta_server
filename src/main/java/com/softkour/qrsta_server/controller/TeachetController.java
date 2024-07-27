package com.softkour.qrsta_server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.course.Course;
import com.softkour.qrsta_server.entity.user.Teacher;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.security.JwtRequestFilter;
import com.softkour.qrsta_server.service.AuthService;
import com.softkour.qrsta_server.service.OTPService;
import com.softkour.qrsta_server.service.course.CourseService;

@RestController
@Validated
@RequestMapping("/api/teacher/")
public class TeachetController {
    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    OTPService otpService;
    @Autowired
    AuthService userService;
    @Autowired
    CourseService courseService;
    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @GetMapping("update_payment_account")
    public ResponseEntity<GenericResponse<Object>> setPaymentAccount(@RequestHeader("account_id") String accountId) {
        User u = MyUtils.getCurrentUserSession(userService);
        Teacher t = u.getTeacher();
        t.setPaymentaccount(accountId);
        userService.save(u);
        return GenericResponse.successWithMessageOnly("success updating");
    }

    @GetMapping("change_payment_mode")
    public ResponseEntity<GenericResponse<Object>> setPaymentMode(
            @RequestHeader(name = "course_id", required = false) Long courseId,
            @RequestHeader("mode") Boolean mode) {

        if (courseId != null) {
            Course course = courseService.findOne(courseId);
            course.setUseOnlinePayment(mode);
            courseService.save(course);
        } else {
            User u = MyUtils.getCurrentUserSession(userService);
            for (Course course : u.getTeacher().getCourses()) {
                course.setUseOnlinePayment(mode);
                courseService.save(course);
            }
        }
        return GenericResponse.successWithMessageOnly("success updating payment mode to: " + mode);
    }

    @GetMapping("change_absence_mode")
    public ResponseEntity<GenericResponse<Object>> setAbsenceMode(
            @RequestHeader(name = "course_id", required = false) Long courseId,
            @RequestHeader("mode") Boolean mode) {

        if (courseId != null) {
            Course course = courseService.findOne(courseId);
            course.setEnableAbsence(mode);
            courseService.save(course);
        } else {
            User u = MyUtils.getCurrentUserSession(userService);
            for (Course course : u.getTeacher().getCourses()) {
                course.setEnableAbsence(mode);
                courseService.save(course);
            }
        }
        return GenericResponse.successWithMessageOnly("success updating absence mode to: " + mode);
    }

    @GetMapping("change_device_mode")
    public ResponseEntity<GenericResponse<Object>> setSwitchDeviceMode(
            @RequestHeader(name = "course_id", required = false) Long courseId,
            @RequestHeader("mode") Boolean mode) {
        if (courseId != null) {
            Course course = courseService.findOne(courseId);
            course.setEnableAutoChangeDevice(mode);
            courseService.save(course);
        } else {
            User u = MyUtils.getCurrentUserSession(userService);
            for (Course course : u.getTeacher().getCourses()) {
                course.setEnableAutoChangeDevice(mode);
                courseService.save(course);
            }
        }
        return GenericResponse.successWithMessageOnly("success updating swich device mode to: " + mode);
    }

    @GetMapping("change_join_mode")
    public ResponseEntity<GenericResponse<Object>> setJoinMode(
            @RequestHeader(name = "course_id", required = false) Long courseId,

            @RequestHeader("mode") Boolean mode) {

        if (courseId != null) {
            Course course = courseService.findOne(courseId);
            course.setEnableAutoJoin(mode);
            courseService.save(course);
        } else {
            User u = MyUtils.getCurrentUserSession(userService);
            for (Course course : u.getTeacher().getCourses()) {
                course.setEnableAutoJoin(mode);
                courseService.save(course);
            }
        }
        return GenericResponse.successWithMessageOnly("success updating join mode to: " + mode);
    }

    @GetMapping("get_settings")
    public ResponseEntity<GenericResponse<Object>> setJoinMode() {
        User u = MyUtils.getCurrentUserSession(userService);
        Map<String, List<Boolean>> m = new HashMap<String, List<Boolean>>();
        m.put("device_mode", u.getTeacher().getCourses().stream().map(e -> e.isEnableAutoChangeDevice()).toList());
        m.put("join_mode", u.getTeacher().getCourses().stream().map(e -> e.isEnableAutoJoin()).toList());
        m.put("payment_mode", u.getTeacher().getCourses().stream().map(e -> e.isUseOnlinePayment()).toList());
        m.put("absence_mode", u.getTeacher().getCourses().stream().map(e -> e.isEnableAbsence()).toList());
        return GenericResponse.success(m);
    }
}
