package com.softkour.qrsta_server.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.softkour.qrsta_server.entity.Course;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.entity.assiociation_entity.StudentCourse;
import com.softkour.qrsta_server.entity.embedded_pk.StudentCoursePK;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.repo.CourseRepository;
import com.softkour.qrsta_server.repo.StudentCourseRepo;

@Service
public class CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseService.class);
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    StudentCourseRepo studentCourseRepo;

    /**
     * Save a course.
     *
     * @param course the entity to save.
     * @return the persisted entity.
     */
    public Course save(Course course) {
        log.debug("Request to save Course : {}", course);
        return courseRepository.save(course);
    }

    public Course addStudentToCourse(User user, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(
                        () -> new ClientException("session", "session not found id: ".concat(courseId.toString())));
        StudentCourse s = studentCourseRepo.save(new StudentCourse(user, course));

        course.addStudent(s);
        return courseRepository.save(course);
    }

    public Course removeStudentFromCourse(User user, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(
                        () -> new ClientException("session", "session not found id: ".concat(courseId.toString())));
        studentCourseRepo.deleteById(new StudentCoursePK(user.getId(), courseId));
        // course.removeStudent(user);
        return courseRepository.save(course);
    }

    /**
     * Partially update a course.
     *
     * @param course the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Course> partialUpdate(Course course) {
        log.debug("Request to partially update Course : {}", course);

        return courseRepository
                .findById(course.getId())
                .map(existingCourse -> {
                    if (course.getId() != null) {
                        existingCourse.setId(course.getId());
                    }
                    if (course.getName() != null) {
                        existingCourse.setName(course.getName());
                    }
                    if (course.getType() != null) {
                        existingCourse.setType(course.getType());
                    }

                    return existingCourse;
                })
                .map(courseRepository::save);
    }

    public Page<Course> findAll(Pageable pageable) {
        log.debug("Request to get all Courses");
        return courseRepository.findAll(pageable);
    }

    public Course findOne(Long id) {
        log.debug("Request to get Course : {}", id);
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
        log.debug("Request to delete Course : {}", id);
        courseRepository.deleteById(id);
    }

    public List<Course> getCourses(Long teacherId) {
        return courseRepository.getCourseByTeacherId(teacherId);
    }
}
