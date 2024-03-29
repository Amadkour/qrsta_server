package com.softkour.qrsta_server.controller;

import java.time.Instant;
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
import com.softkour.qrsta_server.entity.course.Course;
import com.softkour.qrsta_server.entity.course.Schedule;
import com.softkour.qrsta_server.entity.course.Session;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.entity.post.Post;
import com.softkour.qrsta_server.entity.quiz.StudentCourse;
import com.softkour.qrsta_server.entity.user.Student;
import com.softkour.qrsta_server.entity.user.Teacher;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.payload.request.CourseCreationRequest;
import com.softkour.qrsta_server.payload.request.ScheduleRequest;
import com.softkour.qrsta_server.payload.response.CourseResponse;
import com.softkour.qrsta_server.payload.response.SessionAndSocialResponce;
import com.softkour.qrsta_server.payload.response.SessionDetailsStudent;
import com.softkour.qrsta_server.service.AuthService;
import com.softkour.qrsta_server.service.CourseService;
import com.softkour.qrsta_server.service.PostService;
import com.softkour.qrsta_server.service.ScheduleService;
import com.softkour.qrsta_server.service.SessionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/course/")
public class courseController {
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    CourseService courseService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    AuthService authService;
    @Autowired
    PostService postService;
    @Autowired
    SessionService sessionService;

    @PostMapping("add_course")
    public ResponseEntity<GenericResponse<Object>> addCourse(@RequestBody CourseCreationRequest request) {

        /// if update
        if (request.getId() != null) {
            scheduleService.deleteAllAppointmentOfCourse(request.getId());
        }

        // ===========[ schedule ]===============//
        Set<Schedule> savedSchedules = new HashSet<>();
        for (int i = 0; i < request.getSchedules().size(); i++) {
            ScheduleRequest scheduleRequest = request.getSchedules().get(i);
            Schedule schedule = new Schedule();
            schedule.setFromTime(scheduleRequest.getFrom());
            schedule.setToTime(scheduleRequest.getTo());
            schedule.setDay(scheduleRequest.getDay());
            schedule.setId(scheduleRequest.getId());

            savedSchedules.add(scheduleService.save(schedule));
        }
        // ===========[ schedule ]===============//
        Course course = new Course();
        course.setName(request.getName());
        course.setType(request.getCourseType());
        course.setCost(request.getCost());
        course.setSchedules(savedSchedules);
        course.setId(request.getId());

        course.setTeacher(((Teacher) MyUtils.getCurrentUserSession(authService)));

        return GenericResponse.success(courseService.save(course).toCourseResponse());

    }

    @GetMapping("update_cost")
    public ResponseEntity<GenericResponse<CourseResponse>> updateCost(@RequestHeader("course_id") Long courseId,
            @RequestHeader("cost") double cost) {
        Course c = courseService.findOne(courseId);
        c.setCost(cost);
        return GenericResponse.success(courseService.save(c).toCourseResponse());

    }

    @GetMapping("get_course_details")
    public ResponseEntity<GenericResponse<Object>> addCourseDetails(@RequestHeader("course_id") Long courseId) {

        SessionDetailsStudent result = courseService.findOne(courseId).toSessionDetailsStudent();
        result.getStudents().stream().map(e -> e.isActive());
        // result.setStudents(result.getStudents().stream().dropWhile(e ->
        // !e.isActive()).toList());

        return GenericResponse
                .success(result);

    }

    @GetMapping("course_sessions_posts")
    public ResponseEntity<GenericResponse<Object>> getCourseSessionsPosts(
            @RequestHeader(name = "course_id") Long courseId) {
        Set<Session> sessionList = courseService.findOne(courseId).getSessions();
        log.warn(String.valueOf(sessionList.size()));
        List<Post> postslist = postService.posts(courseId);
        return GenericResponse.success(
                new SessionAndSocialResponce(
                        sessionList.stream().map((e) -> e.toSessionDateAndStudentGrade()).toList(),
                        postslist.stream().map((e) -> e.toPostResponce(sessionService, authService)).toList())

        );

    }

    @GetMapping("course_sessions")
    public ResponseEntity<GenericResponse<Object>> getCourseSessions(@RequestHeader(name = "course_id") Long courseId) {
        return GenericResponse.success(
                courseService.findOne(courseId).getSessions().stream().map((e) -> e.toSessionDateAndStudentGrade())
                        .toList());

    }

    @GetMapping("future_course_sessions")
    public ResponseEntity<GenericResponse<Object>> getFutureCourseSessions(
            @RequestHeader(name = "course_id") Long courseId) {
        Instant now = Instant.now();
        return GenericResponse.success(
                courseService.findOne(courseId).getSessions().stream()
                        // .dropWhile(e -> e.getStartDate().isAfter(now))
                        .map((e) -> e.toSessionDateAndStudentGrade())
                        .toList());

    }
    @GetMapping("get_my_courses")
    public ResponseEntity<GenericResponse<Object>> getCourses() {
            User user = MyUtils.getCurrentUserSession(authService);
            if (user.getType() == UserType.TEACHER) {
                List<Course> courseList = courseService.getCourses(user.getId());
                return GenericResponse.success(courseList.stream().map((e) -> e.toCourseResponse()));

            } else {
                List<StudentCourse> courseList = ((Student) user).getCourses().stream().dropWhile(s -> !s.isActive())
                        .toList();
                return GenericResponse.success(courseList.stream().map((e) -> e.getCourse().toCourseResponse()));

            }
        }

        @GetMapping("get_child_courses")
        public ResponseEntity<GenericResponse<Object>> getChildCourses(@RequestHeader("child_phone") String phone) {
            Student student = (Student) authService.getUserByPhoneNumber(phone);
            List<StudentCourse> courseList = student.getCourses().stream().dropWhile(s -> !s.isActive()).toList();
            return GenericResponse.success(courseList.stream().map((e) -> e.getCourse().toCourseResponse()));
    }

    @GetMapping("add_my_to_course")
    public ResponseEntity<GenericResponse<Object>> takeCurrentUserInAttendance(
            @RequestHeader(name = "course_id") Long courseId) {
        try {
            Student u = (Student) MyUtils.getCurrentUserSession(authService);
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
            Student u = (Student) authService.getUserById(userId);
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
