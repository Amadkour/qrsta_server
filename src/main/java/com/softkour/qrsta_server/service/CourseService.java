package com.softkour.qrsta_server.service;

import java.util.List;
import java.util.Optional;

import com.softkour.qrsta_server.entity.Session;
import com.softkour.qrsta_server.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softkour.qrsta_server.entity.Course;
import com.softkour.qrsta_server.repo.CourseRepository;
import org.webjars.NotFoundException;

@Service
public class CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseService.class);
    @Autowired
    CourseRepository courseRepository;

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
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("session not found id: ".concat(courseId.toString())));
        course.addStudent(user);
        return courseRepository.save(course);
    }

    public Course removeStudentFromCourse(User user, Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("session not found id: ".concat(courseId.toString())));
        course.removeStudent(user);
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

    /**
     * Get all the courses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Course> findAll(Pageable pageable) {
        log.debug("Request to get all Courses");
        return courseRepository.findAll(pageable);
    }

    /**
     * Get one course by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Course findOne(Long id) {
        log.debug("Request to get Course : {}", id);
        return courseRepository.findById(id).orElseThrow(() -> new NotFoundException("this course not found id: ".concat(String.valueOf(id))));
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
