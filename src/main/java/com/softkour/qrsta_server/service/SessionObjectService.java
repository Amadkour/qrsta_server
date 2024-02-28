package com.softkour.qrsta_server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softkour.qrsta_server.entity.course.Session;
import com.softkour.qrsta_server.entity.course.SessionObject;
import com.softkour.qrsta_server.entity.course.StudentCourse;
import com.softkour.qrsta_server.entity.enumeration.SessionObjectType;
import com.softkour.qrsta_server.entity.public_entity.StudentSchedule;
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

        return newItem;

    }

}
