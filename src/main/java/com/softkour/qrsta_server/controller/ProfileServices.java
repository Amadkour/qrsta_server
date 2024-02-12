package com.softkour.qrsta_server.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.softkour.qrsta_server.entity.course.Course;
import com.softkour.qrsta_server.entity.quiz.StudentCourse;
import com.softkour.qrsta_server.entity.user.Student;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.payload.request.RequstForm;
import com.softkour.qrsta_server.repo.StudentCourseRepository;
import com.softkour.qrsta_server.service.AuthService;
import com.softkour.qrsta_server.service.CourseService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/profile/")
@Slf4j
public class ProfileServices {
    @Autowired
    CourseService courseService;
    @Autowired
    AuthService authService;
    @Autowired
    StudentCourseRepository studentCourseRepo;

    @GetMapping("requests")
    public ResponseEntity<GenericResponse<List<Map<String, Object>>>> getAllRequestes() {
        User u = MyUtils.getCurrentUserSession(authService);
        List<Course> courses = courseService.getAllDisableStudentsOfCourses(u.getId());
        List<User> users = authService.getNeedToReplaceUsers(u.getId());

        List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
        /// =====================[ course ]================///

        for (Course c : courses) {
            for (StudentCourse s : c.getStudents()) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("type", "course");
                item.put("user_id", s.getId());
                item.put("user_course", s.getCourse().getName());
                item.put("user_image", s.getStudent().getImageUrl());
                item.put("user_name", s.getStudent().getName());
                map.add(item);
            }
        }
        /// =====================[ device ]================///
        for (User user : users) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("type", "device");
            item.put("user_id", user.getId());
            item.put("user_course", user.getCourses().stream().toList().get(0).getCourse().getName());
            item.put("user_image", user.getImageUrl());
            item.put("user_name", user.getName());
            map.add(item);
        }
        return GenericResponse.success(map);

    }

    @GetMapping("request_to_change_device")
    public ResponseEntity<GenericResponse<Object>> requestToChangeDevice() {
        User u = MyUtils.getCurrentUserSession(authService);
        if (u.getStudent() == null)
            throw new ClientException("parent", "this instance not a student");
        if (u.getCourses().stream().allMatch(e -> e.getCourse().getTeacher().getTeacher().isEnableAutoChangeDevice())) {
            u.setRegisterMacAddress(u.getLoginMacAddress());
            authService.save(u);
            return GenericResponse.successWithMessageOnly("update your device successfully");

        } else {
            Student s = u.getStudent();
            s.setNeedToReplace(true);
            u.setStudent(s);
            authService.save(u);
            return GenericResponse.successWithMessageOnly("request send to your teachers successfully");

        }

    }

    @PostMapping("accept")
    public ResponseEntity<GenericResponse<Object>> accept(@RequestHeader("type") String type,
            @RequestBody RequstForm items) {
        String message;
        if (type == "device") {
            message = authService.acceptToChangeDevice(items.getItems());
        } else {
            message = courseService.acceptToJoinCourse(items.getItems());

        }
        return GenericResponse.successWithMessageOnly(message);
    }

    @PostMapping("cancle_request")
    public ResponseEntity<GenericResponse<Object>> cancel(@RequestHeader("type") String type,
            @RequestBody RequstForm items) {
        String message;
        if (type == "device") {
            message = authService.cancleRequest(items.getItems());
        } else {
            message = courseService.cancleRequest(items.getItems());

        }
        return GenericResponse.successWithMessageOnly(message);
    }

    @GetMapping("analysis")
    public ResponseEntity<GenericResponse<Map>> getAnalysis() {
        User u = MyUtils.getCurrentUserSession(authService);
        log.warn(u.getId() + "");
        int allStudents = authService.getAllStudent(u.getId());
        int missingParentStudents = authService.getMissedParents();
        int misedPaymentStudents = studentCourseRepo
                .getStudentByCourse_teacher_idAndLate(u.getId(), 1).size();
        double actalPaymentStudents = studentCourseRepo
                .getStudentByCourse_teacher_idAndLate(u.getId(), 0).stream()
                .mapToDouble(e -> e.getCourse().getCost()).sum();
        double expectedMounthlyProfit = studentCourseRepo
                .getStudentByCourse_teacher_id(u.getId()).stream().mapToDouble(e -> e.getCourse().getCost())
                .sum();

        Map<String, List<Object>> map = new HashMap<String, List<Object>>();
        List<Object> keys = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        keys.add("all_students");
        keys.add("missing_parent");
        keys.add("missing_payment");
        keys.add("expected_monthly_income");
        keys.add("actual_monthly_income");
        /////////
        values.add(allStudents);
        values.add(missingParentStudents);
        values.add(misedPaymentStudents);
        values.add(expectedMounthlyProfit);
        values.add(actalPaymentStudents);
        map.put("keys", keys);
        map.put("values", values);

        return GenericResponse.success(map);
    }

}
