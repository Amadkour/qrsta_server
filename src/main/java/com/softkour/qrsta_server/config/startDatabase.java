package com.softkour.qrsta_server.config;

import com.softkour.qrsta_server.entity.Course;
import com.softkour.qrsta_server.entity.Session;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.repo.UserRepository;
import com.softkour.qrsta_server.service.CourseService;
import com.softkour.qrsta_server.service.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.time.LocalDate;

@Configuration
@Slf4j
public class startDatabase implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CourseService courseService;
    @Autowired
    SessionService sessionService;
    @Override
    public void run(String... args) throws Exception {
User user=new User();
        user.setNationalId("12312312312312");
        user.setType(UserType.TEACHER);
        user.setName("Ahmed Madkour");
        user.setPassword(new BCryptPasswordEncoder().encode("Aa@12345"));
        user.setDob(LocalDate.now());
        user.setPhoneNumber("01110672232");
        user.setMacAddress("aaaa");
        user.setActive(true);
        user.setLogged(true);
       user= userRepository.save(user);
        ///=================course========================///
        Course course=new Course();
        course.setCost(10);
        course.setName("hallow");
        course.setTeacher(user);
        course.addStudent(user);
       course= courseService.save(course);
       userRepository.flush();
        //===================session=====================//
        Session session=new Session();
        session.setCourse(course);
        session.setStartDate(Instant.now());
        session.setEndDate(Instant.now().plusSeconds(600));
        session=sessionService.save(session);
        log.warn("===================================");
        log.warn(session.getId().toString());
        session=sessionService.findOne(1);
        //==========================take attendance=====================
//        log.warn(String.valueOf(session.getStudents().size()));
        session.addStudent(userRepository.findById(1L).get());

        log.warn(String.valueOf(session.getStudents().size()));

        log.warn("===================================");


    }
}
