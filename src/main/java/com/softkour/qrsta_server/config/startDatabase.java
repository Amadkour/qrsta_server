package com.softkour.qrsta_server.config;

import com.softkour.qrsta_server.entity.Course;
import com.softkour.qrsta_server.entity.Offer;
import com.softkour.qrsta_server.entity.Session;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.repo.UserRepository;
import com.softkour.qrsta_server.service.CourseService;
import com.softkour.qrsta_server.service.OfferService;
import com.softkour.qrsta_server.service.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.stream.Collectors;

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

    @Override
    public void run(String... args) throws Exception {
        for (int i = 1; i < 4; i++) {

            User user = new User();
            user.setNationalId("1231231231231".concat(String.valueOf(i)));
            user.setType(UserType.TEACHER);
            user.setName("Ahmed Madkour");
            user.setPassword(new BCryptPasswordEncoder().encode("Aa@12345"));
            user.setDob(LocalDate.now());
            user.setPhoneNumber("0111067223".concat(String.valueOf(i)));
            user.setMacAddress("aaaa");
            user.setActive(true);
            user.setLogged(true);
            user = userRepository.save(user);

            ///=================course========================///
            Course course = new Course();
            course.setCost(10);
            course.setName("course".concat(String.valueOf(i)));
            course.setTeacher(user);
            log.warn("==========================================");
            log.warn(userRepository.findAll().stream().map(User::getName).toList().toString());
            log.warn("==========================================");
            course.setStudents(userRepository.findAll().stream().collect(Collectors.toSet()));
            course.addStudent(user);
            course = courseService.save(course);
            log.warn("==========================================");
            log.warn(course.getStudents().stream().map(e->e.getId()).collect(Collectors.toSet()).toString());
            log.warn("==========================================");
            //===================session=====================//
            Session session = new Session();
            session.setCourse(course);
            session.setStartDate(Instant.now());
            session.setEndDate(Instant.now().plusSeconds(600));
            session = sessionService.save(session);
            session.addStudent(user);
            sessionService.save(session);
            //==========================offer=====================
            Offer offer = new Offer();
            offer.setCost(100);
            offer.setEndDate(LocalDate.now());
            offer.setCourse(course);
            offer.setMonths(3);
            offerService.createOffer(offer);
        }

    }
}
