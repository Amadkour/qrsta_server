package com.softkour.qrsta_server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.course.Schedule;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.repo.ScheduleRepository;

@Service
public class ScheduleService {

    @Autowired
    AuthService authService;
    @Autowired
    ScheduleRepository scheduleRepository;

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Schedule update(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> userSchedule() {
        User u = MyUtils.getCurrentUserSession(authService);
        if (u.getType() == UserType.TEACHER) {
            return teacherSchedule(u.getId());
        } else {
            return studentSchedule(u.getId());
        }
    }

    public List<Schedule> teacherSchedule(Long teacherId) {
        return scheduleRepository.findAllByCourses_teacher_Id(teacherId);
    }

    public List<Schedule> studentSchedule(Long studentId) {
        return scheduleRepository.findAllByCourses_students_student_Id(studentId);
    }

    public Optional<Schedule> partialUpdate(Schedule schedule) {

        return scheduleRepository
                .findById(schedule.getId())
                .map(existingSchedule -> {
                    if (schedule.getId() != null) {
                        existingSchedule.setId(schedule.getId());
                    }
                    if (schedule.getDay() != null) {
                        existingSchedule.setDay(schedule.getDay());
                    }
                    if (schedule.getFromTime() != null) {
                        existingSchedule.setFromTime(schedule.getFromTime());
                    }
                    if (schedule.getToTime() != null) {
                        existingSchedule.setToTime(schedule.getToTime());
                    }

                    return existingSchedule;
                })
                .map(scheduleRepository::save);
    }

    @Transactional(readOnly = true)
    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public Page<Schedule> findAllWithEagerRelationships(Pageable pageable) {
        return scheduleRepository.findAllWithEagerRelationships(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Schedule> findOne(Long id) {
        return scheduleRepository.findOneWithEagerRelationships(id);
    }

    @Transactional
    public void delete(Long id) {
        scheduleRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllAppointmentOfCourse(Long courseId) {
        scheduleRepository.deleteAllByCourses_id(courseId);
    }
}
