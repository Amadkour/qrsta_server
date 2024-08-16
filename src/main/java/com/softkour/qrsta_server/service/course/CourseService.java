package com.softkour.qrsta_server.service.course;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.softkour.qrsta_server.entity.course.Course;
import com.softkour.qrsta_server.entity.course.StudentCourse;
import com.softkour.qrsta_server.entity.enumeration.CourseType;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.payload.request.AcceptRequest;
import com.softkour.qrsta_server.repo.course.CourseRepository;
import com.softkour.qrsta_server.repo.course.StudentCourseRepository;

@Service
public class CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseService.class);
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    StudentCourseRepository studentCourseRepository;

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public Integer getAppPayment(Long userId, Long courseId) {
        return courseRepository
                .getCourseByIdAndStudents_student_user_id(courseId, userId)
                .getStudents().iterator().next().getAppPaymentLate();
    }

    public Integer getCoursePayment(Long userId, Long courseId) {
        return courseRepository
                .getCourseByIdAndStudents_student_user_id(courseId, userId)
                .getStudents().iterator().next().getCoursePaymentLate();
    }

    public Course addStudentToCourse(User user, Long courseId) {
        if (user.getStudent().getCourses().stream().anyMatch(e -> e.getCourse().getId().compareTo(courseId) == 0)) {
            throw new ClientException("course", "you are aready joined in this course: ".concat(courseId.toString()));

        } else {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(
                            () -> new ClientException("course", "course not found id: ".concat(courseId.toString())));
            StudentCourse student = new StudentCourse();
            student.setCourse(course);
            student.setStudent(user.getStudent());
            student.setAppPaymentLate(1);
            student.setCoursePaymentLate(1);
            if (course.getType() == CourseType.PRIVATE
                    && course.isEnableAutoJoin() == false) {
                student.setActive(false);
            } else {
                student.setActive(true);

            }

            course.addStudent(student);
            return courseRepository.save(course);
        }
    }

    public Course removeStudentFromCourse(User user, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(
                        () -> new ClientException("session", "session not found id: ".concat(courseId.toString())));
        // course.removeStudent(user);
        return courseRepository.save(course);
    }

    public Page<Course> findAll(Pageable pageable) {
        log.debug("Request to get all Courses");
        return courseRepository.findAll(pageable);
    }

    public Course findOne(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(
                        () -> new ClientException("course", "this course not found id: ".concat(String.valueOf(id))));
    }

    /**
     * Delete the course by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    public List<Course> getCourses(Long teacherId) {
        return courseRepository.getCourseByTeacher_user_id(teacherId);
    }

    public List<Course> getAllDisableStudentsOfCourses(Long teacherId) {
        return courseRepository.getCourseByTeacher_idAndStudents_active(teacherId, false);
    }

    // public Course getActiveCourse(Long courseId) {
    // return courseRepository.getCourseByIdAndStudents_active(courseId, true);
    // }

    public String acceptToJoinCourse(List<AcceptRequest> requests) {
        for (AcceptRequest request : requests) {
            StudentCourse user = studentCourseRepository.findById(Long.parseLong(request.getId())).orElseThrow(
                    () -> new ClientException("course",
                            "this course not found id: ".concat(String.valueOf(request.getId()))));
            log.warn(user.isActive() + "");
            user.setActive(true);
            studentCourseRepository.save(user);
        }
        return "accept to join successfully";

    }

    public String cancleRequest(List<AcceptRequest> requests) {
        for (AcceptRequest request : requests) {
            StudentCourse user = studentCourseRepository.findById(Long.parseLong(request.getId())).orElseThrow(
                    () -> new ClientException("course",
                            "this course not found id: ".concat(String.valueOf(request.getId()))));
            studentCourseRepository.delete(user);
        }
        return "cancle join request successfully";

    }
}
