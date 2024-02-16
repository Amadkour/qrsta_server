package com.softkour.qrsta_server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softkour.qrsta_server.entity.course.Session;
import com.softkour.qrsta_server.entity.course.SessionObject;
import com.softkour.qrsta_server.entity.enumeration.SessionObjectType;
import com.softkour.qrsta_server.entity.public_entity.StudentSchedual;
import com.softkour.qrsta_server.entity.quiz.StudentCourse;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.repo.SessionObjectRepo;
import com.softkour.qrsta_server.repo.StudentScheduleRepo;

@Service
public class SessionObjectService {
    @Autowired
    SessionObjectRepo sessionObjectRepo;
    @Autowired
    StudentScheduleRepo scheduleRepo;
    public SessionObject save(String title, SessionObjectType type, Long parentId, Session session) {
        SessionObject newItem = new SessionObject();
        newItem.setTitle(title);
        newItem.setType(type);
        newItem.setSession(session); /// add sub object
        if (type == SessionObjectType.ANSWER || type == SessionObjectType.SECONDARY_OBJECT) {
            SessionObject sessionObject = sessionObjectRepo.findById(parentId).orElseThrow(
                    () -> new ClientException("parentId", "object id not found:".concat(parentId.toString())));
            sessionObject.addSubItem(newItem);
            sessionObjectRepo.save(sessionObject);
            return newItem;
        }
        newItem = sessionObjectRepo.save(newItem);
        /// add it in student schedual
        List<StudentCourse> students = session.getCourse().getStudents().stream().collect(Collectors.toList());
        for (int i = 0; i < students.size(); i++) {
            StudentSchedual item = new StudentSchedual();
            item.setDone(false);
            item.setRead(false);
            item.setObject(newItem);
            item.setUser(students.get(i).getStudent());

            scheduleRepo.save(item);
        }
        return newItem;

    }

}
