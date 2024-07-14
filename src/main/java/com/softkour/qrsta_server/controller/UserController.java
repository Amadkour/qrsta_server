package com.softkour.qrsta_server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.course.Course;
import com.softkour.qrsta_server.entity.course.Session;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.payload.request.ParentRegisterRequest;
import com.softkour.qrsta_server.payload.request.UpdateUserRequest;
import com.softkour.qrsta_server.payload.response.AbstractChild;
import com.softkour.qrsta_server.payload.response.AbstractUser;
import com.softkour.qrsta_server.repo.public_repo.NotificationRepo;
import com.softkour.qrsta_server.security.JwtRequestFilter;
import com.softkour.qrsta_server.service.AuthService;
import com.softkour.qrsta_server.service.OTPService;
import com.softkour.qrsta_server.service.course.CourseService;

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/api/user/")
public class UserController {
    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    OTPService otpService;
    @Autowired
    CourseService courseService;
    @Autowired
    AuthService userService;
    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Autowired
    NotificationRepo notificationRepo;
    @Autowired
    AuthService authService;

    @PostMapping("update")
    public ResponseEntity<GenericResponse<Object>> saveUser(
            @RequestBody @Valid UpdateUserRequest registerationRequest) {

        User u = userService.update(registerationRequest);

        return GenericResponse.success(u.toUpdateResponse());

    }

    @GetMapping("logout")
    public ResponseEntity<GenericResponse<Object>> logout() {
        User user = MyUtils.getCurrentUserSession(userService);
        user.setLogoutTimes(user.getLogoutTimes() + 1);
        userService.save(user);
        return GenericResponse.successWithMessageOnly("logout successfully");
    }

    @PostMapping("/parent_register")
    public ResponseEntity<GenericResponse<Object>> createParent(
            @RequestBody @Valid ParentRegisterRequest parentRegisterRequest) {
        return GenericResponse
                .successWithMessageOnly(userService.createParent(parentRegisterRequest));
    }

    @PostMapping("verify_parent_otp")
    public ResponseEntity<GenericResponse<Object>> verifyOtp(@RequestHeader("parent_otp") String otp,
            @RequestHeader("parent_phone_number") String parentPhone,
            @RequestHeader("parent_phone_number_code") String parentPhoneCode) {
        userService.verifyParentUser(otp, parentPhone, MyUtils.getCurrentUserSession(userService));
        return GenericResponse.successWithMessageOnly("Yor parent created Successffly");
    }

    @GetMapping("get_children")
    public ResponseEntity<GenericResponse<List<AbstractChild>>> getChildren() {
        return GenericResponse
                .success(userService.getChildren(userService).stream().map((e) -> e.toAbstractChild()).toList());
    }

    @GetMapping("get_parent")
    public ResponseEntity<GenericResponse<AbstractUser>> getParent() {
        return GenericResponse
                .success(userService.getParent(userService).toAbstractUser());
    }

    @GetMapping("delete_child")
    public ResponseEntity<GenericResponse<Object>> deleteChild(@RequestHeader("child_id") Long childId) {
        userService.deleteChild(childId);
        return GenericResponse
                .successWithMessageOnly("unlink child successfully");
    }

    @GetMapping("add_child")
    public ResponseEntity<GenericResponse<Object>> addChild(@RequestHeader("child_id") String childId) {
        return GenericResponse
                .success(userService.addChild(userService, childId).toAbstractChild());
    }

    @GetMapping("child_statistics")
    public ResponseEntity<GenericResponse<Object>> getChildStatistics(@RequestHeader("child_id") Long childId,
            @RequestHeader("course_id") Long courseId) {
        User u = MyUtils.getCurrentUserSession(authService);
        int childernCount = userService.getChildren(userService).size();
        Map<String, Object> map = new HashMap<String, Object>();
        // =============[ check payment ]===============//
        System.out.println(u.getType().toString());
        System.out.println(u.getParent());
        if (u.getParent().getLate() > 0)
            throw new ClientException(
                    "payment late monthes:" + u.getParent().getLate() + ",cost of month is:"
                            + MyUtils.parentCost * childernCount
                            + ",of course id is:" + courseId,
                    "", 999);
        Course course = courseService.findOne(courseId);
        Set<Session> sessions = course.getSessions();
        map.put("attendance", sessions.stream()
                .map(s -> s.getStudents().stream()
                        .anyMatch(b -> b.getUser().getId() == childId))
                .toList());

        // map.put("late", userService.getUserLatePayment(childId, courseId));
        map.put("late_in_app_payment", courseService.getAppPayment(childId, courseId));
        System.out.println(course.isUseOnlinePayment());
        if (course.isUseOnlinePayment()) {
            map.put("late_in_course_payment", courseService.getCoursePayment(childId, courseId));
        }
        map.put("score", userService.getUserScore(childId, courseId));
        return GenericResponse.success(map);
    }

}
