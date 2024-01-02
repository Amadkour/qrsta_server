package com.softkour.qrsta_server.payload.mapper;

import com.softkour.qrsta_server.entity.Session;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.payload.response.SessionDetailsStudent;

import java.util.stream.Collectors;

public class SessionStudentsMapper {

    public SessionDetailsStudent toDTOs(Session session) {
        return new SessionDetailsStudent(session.getStudents().stream().map(new UserLogoMapper()::toDto).collect(Collectors.toSet()));

    }
}
