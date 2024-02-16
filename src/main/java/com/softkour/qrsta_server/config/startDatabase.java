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
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.entity.public_entity.Country;
import com.softkour.qrsta_server.entity.quiz.StudentCourse;
import com.softkour.qrsta_server.entity.user.Student;
import com.softkour.qrsta_server.entity.user.Teacher;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.repo.CountryRepo;
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
    CountryRepo countryRepo;
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
        /// =======country==============///
        Country eg = new Country();
        eg.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/f/fe/Flag_of_Egypt.svg");
        eg.setName("egypt");
        eg.setSidMax("14");
        eg.setSidMin("14");
        eg.setPhoneLength("10");
        eg.setPhoneStart("3|2");
        eg.setPhoneCode("+20");

        Country saudi = new Country();
        saudi.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/0/0d/Flag_of_Saudi_Arabia.svg");
        saudi.setName("saudi");
        saudi.setSidMax("10");
        saudi.setSidMin("10");
        saudi.setPhoneStart("3|2");
        saudi.setPhoneLength("8");
        saudi.setPhoneCode("+966");
        Country emirate = new Country();
        emirate.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/c/cb/Flag_of_the_United_Arab_Emirates.svg");
        emirate.setName("emirate");
        emirate.setSidMax("10");
        emirate.setSidMin("10");
        emirate.setPhoneLength("8");
        emirate.setPhoneCode("+971");

        countryRepo.save(eg);
        countryRepo.save(emirate);
        countryRepo.save(saudi);
        for (int i = 2; i < 5; i++) {

            User user = new User();
            user.setNationalId("1231231231231".concat(String.valueOf(i)));
            if (2 == i) {
                user.setType(UserType.TEACHER);
                user.setTeacher(new Teacher());
            } else {
                user.setType(UserType.STUDENT);
                user.setStudent(new Student());
            }
            user.setName("Ahmed Madkour ".concat(String.valueOf(i)));
            user.setPassword(new BCryptPasswordEncoder().encode("Aa@12345"));
            user.setDob(LocalDate.now());
            user.setPhoneNumber("+20111067222".concat(String.valueOf(i)));
            user.setCountryCode("+20");
            user.setRegisterMacAddress("aaaa");
            user.setActive(true);
            user.setLogged(true);
            user = userRepository.save(user);
            if (i >= 3) {
                Student s = user.getStudent();
                s.setParent(userRepository.findUserByPhoneNumber("+201110672222"));
                user.setStudent(s);
                user = userRepository.save(user);
            }
            /// =================course========================///
            Course course = new Course();
            course.setCost(10);
            course.setUseOnlinePayment(true);
            course.setName("course".concat(String.valueOf(i)));
            course.setTeacher(user);
            course.setType(CourseType.PUBLIC);
            course = courseService.save(course);
            log.warn("==========================================");
            log.warn(userRepository.findAll().stream().map(User::getName).toList().toString());
            log.warn("==========================================");
            StudentCourse studentCourse = new StudentCourse();
            studentCourse.setCourse(course);
            studentCourse.setStudent(user);
            studentCourse.setLate(0);
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
                studentC.setStudent(users.get(u));
                studentC.setLate(1);
                course.addStudent(studentC);
            }

            course = courseService.save(course);
            // ===================session=====================//
            // Session session = new Session();
            // session.setLabel("session" +
            // sessionService.findSessionsOfCourse(course.getId()).size());
            // session.setCourse(course);
            // session.setStartDate(Instant.now());
            // session.setEndDate(Instant.now().plusSeconds(5));
            // session = sessionService.save(session);
            // session.addStudent(user);
            // sessionService.save(session);
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
