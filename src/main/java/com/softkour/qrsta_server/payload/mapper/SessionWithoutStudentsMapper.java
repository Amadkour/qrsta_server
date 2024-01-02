package com.softkour.qrsta_server.payload.mapper;

import com.softkour.qrsta_server.entity.Session;
import com.softkour.qrsta_server.payload.response.SessionDetailsWithoutStudents;
public class SessionWithoutStudentsMapper {

  public  SessionDetailsWithoutStudents toDTOs(Session session) {
        return new SessionDetailsWithoutStudents(session.getObjects(), session.getStartDate(), session.getEndDate());
    }
}
