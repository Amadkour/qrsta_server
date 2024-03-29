package com.softkour.qrsta_server.config;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.softkour.qrsta_server.entity.course.Course;
import com.softkour.qrsta_server.entity.course.Offer;
import com.softkour.qrsta_server.entity.course.Schedule;
import com.softkour.qrsta_server.entity.course.Session;
import com.softkour.qrsta_server.entity.enumeration.CourseType;
import com.softkour.qrsta_server.entity.quiz.StudentCourse;
import com.softkour.qrsta_server.entity.user.Student;
import com.softkour.qrsta_server.entity.user.Teacher;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.repo.StudentCourseRepository;
import com.softkour.qrsta_server.repo.UserRepository;
import com.softkour.qrsta_server.service.CourseService;
import com.softkour.qrsta_server.service.OfferService;
import com.softkour.qrsta_server.service.PostService;
import com.softkour.qrsta_server.service.SessionService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class startDatabase implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CourseService courseService;
    @Autowired
    SessionService sessionService;
    @Autowired
    OfferService offerService;
    @Autowired
    PostService postService;
    @Autowired
    StudentCourseRepository studentCourseRepo;

    @Override
    public void run(String... args) throws Exception {
        for (int i = 2; i < 5; i++) {

            User user;
            /// =================course========================///
            Course course = new Course();
            course.setCost(10);
            course.setName("course".concat(String.valueOf(i)));
            course.setType(CourseType.PUBLIC);
            course = courseService.save(course);
            log.warn("==========================================");
            log.warn(userRepository.findAll().stream().map(User::getName).toList().toString());
            log.warn("==========================================");
            StudentCourse studentCourse = new StudentCourse();
            studentCourse.setCourse(course);
            studentCourse.setLate(0);
            if (2 == i) {
                user = new Teacher();
                course.setTeacher(((Teacher) user));

            } else {
                user = new Student();
                studentCourse.setStudent(((Student) user));

            }

            user.setNationalId("1231231231231".concat(String.valueOf(i)));


            user.setName("Ahmed Madkour ".concat(String.valueOf(i)));
            user.setPassword(new BCryptPasswordEncoder().encode("Aa@12345"));
            user.setDob(LocalDate.now());
            user.setPhoneNumber("111067223".concat(String.valueOf(i)));
            user.setCountryCode("20");
            user.setRegisterMacAddress("aaaa");
            user.setActive(true);
            user.setLogged(true);
            user = userRepository.save(user);


            // course.addStudent(studentCourse);
            course.addStudent(studentCourse);
            Schedule schedule = new Schedule();
            schedule.setDay("monday");
            schedule.setFromTime("02:00 PM");
            schedule.setToTime("04:00 PM");
            course.addSchedule(schedule);
            List<User> users = userRepository.findAll();
            for (int u = 0; u < users.size(); u++) {
                StudentCourse studentC = new StudentCourse();
                studentC.setCourse(course);
                studentC.setActive(true);
                if (users.get(u) instanceof Student)
                    studentC.setStudent(((Student) users.get(u)));
                studentC.setLate(u);
                course.addStudent(studentC);
            }

            course = courseService.save(course);
            // ===================session=====================//
            Session session = new Session();
            session.setCourse(course);
            session.setStartDate(Instant.now());
            session.setEndDate(Instant.now().plusSeconds(5));
            session = sessionService.save(session);
            if (user instanceof Student)
                session.addStudent(((Student) user));
            sessionService.save(session);
            // ==========================offer=====================
            Offer offer = new Offer();
            Set<Course> cs = new HashSet<Course>();
            cs.add(course);
            offer.setCost("100");
            offer.setEndDate(Instant.now());
            offer.setCourses(cs);
            offer.setMonths(3);
            offerService.save(offer);
            // =======================[post]==================
            // Post post = new Post();
            // post.setData("first Post");
            // post.setOwner(user.toAbstractUser());
            // post.setSession(session);
            // postService.addPost(post);

        }

    }
}
