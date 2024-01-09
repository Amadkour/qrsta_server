package com.softkour.qrsta_server.config;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.softkour.qrsta_server.entity.Course;
import com.softkour.qrsta_server.entity.Offer;
import com.softkour.qrsta_server.entity.Session;
import com.softkour.qrsta_server.entity.StudentCourse;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.repo.StudentCourseRepo;
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
    StudentCourseRepo studentCourseRepo;

    @Override
    public void run(String... args) throws Exception {
        for (int i = 1; i < 4; i++) {

            User user = new User();
            user.setNationalId("1231231231231".concat(String.valueOf(i)));
            user.setType(UserType.TEACHER);
            user.setName("Ahmed Madkour ".concat(String.valueOf(i)));
            user.setPassword(new BCryptPasswordEncoder().encode("Aa@12345"));
            user.setDob(LocalDate.now());
            user.setPhoneNumber("111067223".concat(String.valueOf(i)));
            user.setCountryCode("20");
            user.setMacAddress("aaaa");
            user.setActive(true);
            user.setLogged(true);
            user = userRepository.save(user);

            /// =================course========================///
            Course course = new Course();
            course.setCost(10);
            course.setName("course".concat(String.valueOf(i)));
            course.setTeacher(user);
            course = courseService.save(course);
            log.warn("==========================================");
            log.warn(userRepository.findAll().stream().map(User::getName).toList().toString());
            log.warn("==========================================");
            StudentCourse studentCourse = new StudentCourse();
            studentCourse.setCourse(course);
            studentCourse.setStudent(user);
            studentCourse.setLateMonthes(0);
            course.addStudent(studentCourse);
            List<User> users = userRepository.findAll();
            for (int u = 0; u < users.size(); u++) {
                StudentCourse studentC = new StudentCourse();
                studentC.setCourse(course);
                studentC.setStudent(users.get(u));
                studentC.setLateMonthes(u);
                course.addStudent(studentC);
            }
            course = courseService.save(course);
            // ===================session=====================//
            Session session = new Session();
            session.setCourse(course);
            session.setStartDate(Instant.now());
            session.setEndDate(Instant.now().plusSeconds(600));
            session = sessionService.save(session);
            session.addStudent(user);
            sessionService.save(session);
            // ==========================offer=====================
            Offer offer = new Offer();
            offer.setCost(100);
            offer.setEndDate(LocalDate.now());
            offer.setCourse(course);
            offer.setMonths(3);
            offerService.createOffer(offer);
            // =======================[post]==================
            // Post post = new Post();
            // post.setData("first Post");
            // post.setOwner(user.toAbstractUser());
            // post.setSession(session);
            // postService.addPost(post);

        }

    }
}
