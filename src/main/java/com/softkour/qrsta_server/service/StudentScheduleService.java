package com.softkour.qrsta_server.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.enumeration.NotificationType;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.entity.public_entity.StudentSchedule;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.repo.StudentScheduleRepo;
import com.softkour.qrsta_server.service.public_service.NotificationService;

@Service
public class StudentScheduleService {
    @Autowired
    StudentScheduleRepo scheduleRepo;
    @Autowired
    NotificationService notificationService;
    @Autowired
    AuthService authService;

    public List<StudentSchedule> getUserSchedule(Long userId) {
        return scheduleRepo.getScheduleByUser_idAndDoneFalseAndCreatedDateAfterOrderByCreatedDateDesc(userId,
                Instant.now().minus(3, ChronoUnit.DAYS));
    }

    public List<StudentSchedule> getTeacherSchedule(Long userId) {
        return scheduleRepo.getScheduleByCourse_teacher_idAndQuestionNotNullOrderByCreatedDateDesc(userId);
    }

    public StudentSchedule correct(Long scheduleId, List<String> correctAnswer) {
        StudentSchedule item = scheduleRepo.findById(scheduleId)
                .orElseThrow(() -> new ClientException("item", "this item Not Found"));
        List<String> corrected = item.getQuestion().getOptions().stream().takeWhile(e -> e.getIsCorrectAnswer())
                .map(e -> e.getTitle()).toList();
        Boolean result = correctAnswer.containsAll(corrected) && corrected.size() == correctAnswer.size();
        if (result) {
            item.setDone(true);
            scheduleRepo.save(item);
            return item;
        }
        User u = MyUtils.getCurrentUserSession(authService);
        if (u.getType() == UserType.STUDENT) {
            Set<User> users = new HashSet<>();
            users.add(u.getStudent().getParent());
            notificationService.addNotification(NotificationType.REVISION,
                    "hello sir, your son " + u.getName() + "not reviewed successfully", scheduleId, users);
        }
        return item;

    }

    public StudentSchedule done(Long itemId) {
        StudentSchedule item = scheduleRepo.findById(itemId)
                .orElseThrow(() -> new ClientException("item", "this item Not Found"));
        item.setDone(true);
        item = scheduleRepo.save(item);
        return item;
    }

}
