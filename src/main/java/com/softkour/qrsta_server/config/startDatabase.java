package com.softkour.qrsta_server.config;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.softkour.qrsta_server.entity.course.Course;
import com.softkour.qrsta_server.entity.course.Schedule;
import com.softkour.qrsta_server.entity.course.StudentCourse;
import com.softkour.qrsta_server.entity.enumeration.CourseType;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.entity.public_entity.AppVersion;
import com.softkour.qrsta_server.entity.user.Parent;
import com.softkour.qrsta_server.entity.user.Student;
import com.softkour.qrsta_server.entity.user.Teacher;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.repo.CountryRepo;
import com.softkour.qrsta_server.repo.ParentReppo;
import com.softkour.qrsta_server.repo.course.StudentCourseRepository;
import com.softkour.qrsta_server.repo.UserRepository;
import com.softkour.qrsta_server.repo.public_repo.AppVersionRepo;
import com.softkour.qrsta_server.repo.user.TeacherRepo;
import com.softkour.qrsta_server.service.OfferService;
import com.softkour.qrsta_server.service.PostService;
import com.softkour.qrsta_server.service.SessionService;
import com.softkour.qrsta_server.service.course.CourseService;

@Configuration
public class startDatabase implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ParentReppo parentRepository;
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
    @Autowired
    AppVersionRepo appVersionRepo;
    @Autowired
    TeacherRepo teacherRepo;

    @Override
    public void run(String... args) throws Exception {
        AppVersion ios = new AppVersion();
        ios.setAppRelease("this App first App");
        ios.setAvailable(true);
        ios.setIos(true);
        ios.setVersion("1");
        appVersionRepo.save(ios);
        AppVersion android = new AppVersion();
        android.setAppRelease("this App first App");
        android.setAvailable(true);
        android.setIos(false);
        android.setVersion("1");
        appVersionRepo.save(android);
        /// =======country==============///
        // Country eg = new Country();
        // eg.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/f/fe/Flag_of_Egypt.svg");
        // eg.setName("egypt");
        // eg.setSidMax("14");
        // eg.setSidMin("14");
        // eg.setPhoneLength("10");
        // eg.setPhoneStart("3|2");
        // eg.setPhoneCode("+20");

        // Country saudi = new Country();
        // saudi.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/0/0d/Flag_of_Saudi_Arabia.svg");
        // saudi.setName("saudi");
        // saudi.setSidMax("10");
        // saudi.setSidMin("10");
        // saudi.setPhoneStart("3|2");
        // saudi.setPhoneLength("8");
        // saudi.setPhoneCode("+966");
        // Country emirate = new Country();
        // emirate.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/c/cb/Flag_of_the_United_Arab_Emirates.svg");
        // emirate.setName("emirate");
        // emirate.setSidMax("10");
        // emirate.setSidMin("10");
        // emirate.setPhoneLength("8");
        // emirate.setPhoneCode("+971");

        // countryRepo.save(eg);
        // countryRepo.save(emirate);
        // countryRepo.save(saudi);
        try {
            for (int i = 2; i < 7; i++) {

                User user = new User();
                user.setNationalId("1231231231231".concat(String.valueOf(i)));
                if (i >= 4) {
                    user.setType(UserType.STUDENT);
                    Student s = new Student();
                    User u = userRepository.findUserByPhoneNumber("+201110672222")
                            .orElseThrow(() -> new ClientException("use", "ser Not Found"));

                    s.setParent(u);
                    user.setStudent(s);
                    s.setUser(user);
                    if (i == 4) {
                        user.setName("Ahmed Madkour ".concat(String.valueOf(i)));
                    } else if (i == 5) {
                        user.setName("Mohamed Mostafa ".concat(String.valueOf(i)));
                    } else {
                        user.setName("Ali Ahmed ".concat(String.valueOf(i)));

                    }
                    user.setPassword(new BCryptPasswordEncoder().encode("Aa@12345"));
                    user.setDob(LocalDate.now().minusYears(18));
                    user.setPhoneNumber("+20111067222".concat(String.valueOf(i)));
                    user.setCountryCode("+20");
                    user.setRegisterMacAddress("aaaa");
                    user.setActive(true);
                    user.setLogged(true);
                    user = userRepository.save(user);
                } else if (3 == i) {
                    user.setType(UserType.TEACHER);
                    user.setName("Ahmed Madkour ".concat(String.valueOf(i)));
                    user.setPassword(new BCryptPasswordEncoder().encode("Aa@12345"));
                    user.setDob(LocalDate.now().minusYears(18));
                    user.setPhoneNumber("+20111067222".concat(String.valueOf(i)));
                    user.setCountryCode("+20");
                    user.setRegisterMacAddress("aaaa");
                    user.setActive(true);
                    user.setLogged(true);

                    user = userRepository.save(user);
                    Teacher teacher = new Teacher();
                    teacher.setUser(user);
                    teacherRepo.save(teacher);

                } else {

                    user.setType(UserType.OBSERVER);
                    user.setName("Ahmed Madkour ".concat(String.valueOf(i)));
                    user.setPassword(new BCryptPasswordEncoder().encode("Aa@12345"));
                    user.setDob(LocalDate.now().minusYears(18));
                    user.setPhoneNumber("+20111067222".concat(String.valueOf(i)));
                    user.setCountryCode("+20");
                    user.setRegisterMacAddress("aaaa");
                    user.setActive(true);
                    user.setLogged(true);
                    user = userRepository.save(user);
                    Parent p = new Parent();
                    p.setUser(user);
                    p.setLate(1);
                    user.setParent(p);
                    user = userRepository.save(user);

                }
            }
            /// =================course========================///
            User teacher = userRepository.findUserByPhoneNumber("+201110672223")
                    .orElseThrow(() -> new ClientException("use", "user Not Found"));
            User student1 = userRepository.findUserByPhoneNumber("+201110672224")
                    .orElseThrow(() -> new ClientException("use", "ser Not Found"));
            User student2 = userRepository.findUserByPhoneNumber("+201110672225")
                    .orElseThrow(() -> new ClientException("use", "ser Not Found"));
            User student3 = userRepository.findUserByPhoneNumber("+201110672226")
                    .orElseThrow(() -> new ClientException("use", "ser Not Found"));
            Course course = new Course();
            course.setCost(200);
            course.setUseOnlinePayment(true);
            course.setName("Media Creator");
            course.setTeacher(teacher.getTeacher());
            course.setType(CourseType.PUBLIC);
            course.setUseOnlinePayment(false);

            Course course2 = new Course();
            course2.setCost(150);
            course2.setUseOnlinePayment(true);
            course2.setName("Flutter Course");
            course2.setTeacher(teacher.getTeacher());
            course2.setType(CourseType.PUBLIC);
            course2.setUseOnlinePayment(false);

            /// std-1
            StudentCourse studentCourse1 = new StudentCourse();
            studentCourse1.setCourse(course);
            studentCourse1.setStudent(student1.getStudent());
            studentCourse1.setAppPaymentLate(1);
            studentCourse1.setCoursePaymentLate(1);
            studentCourse1.setActive(true);
            course.addStudent(studentCourse1);
            /// std-1
            StudentCourse studentCourse2 = new StudentCourse();
            studentCourse2.setCourse(course);
            studentCourse2.setStudent(student2.getStudent());
            studentCourse1.setAppPaymentLate(1);
            studentCourse1.setCoursePaymentLate(1);
            studentCourse2.setActive(true);
            course.addStudent(studentCourse2);
            /// std-1
            StudentCourse studentCourse3 = new StudentCourse();
            studentCourse3.setCourse(course);
            studentCourse3.setStudent(student3.getStudent());
            studentCourse1.setAppPaymentLate(1);
            studentCourse1.setCoursePaymentLate(1);
            studentCourse3.setActive(true);
            course.addStudent(studentCourse3);
            Schedule schedule = new Schedule();
            schedule.setDay("monday");
            schedule.setFromTime("02:00 PM");
            schedule.setToTime("04:00 PM");
            course.addSchedule(schedule);
            /// std-1
            StudentCourse studentCourse4 = new StudentCourse();
            studentCourse4.setCourse(course2);
            studentCourse4.setStudent(student1.getStudent());
            studentCourse4.setAppPaymentLate(1);
            studentCourse4.setCoursePaymentLate(1);
            studentCourse4.setActive(true);
            course2.addStudent(studentCourse4);
            courseService.save(course);
            courseService.save(course2);
            System.out.println(courseService.getCourses(teacher.getId()));
        }catch (Exception ignored){}finally {
            System.out.println("============[done all]");

        }
    }
}
