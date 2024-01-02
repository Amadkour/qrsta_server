package com.softkour.qrsta_server.payload.mapper;

import com.softkour.qrsta_server.entity.Quiz;
import com.softkour.qrsta_server.entity.Session;
import com.softkour.qrsta_server.payload.response.SessionDateAndStudentGrade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.DoubleStream;

public class SessionDateAndStudentGradeMapper {
    public  SessionDateAndStudentGrade toDto(Session session) {
        double grade = session.getQuizzes().stream().reduce((first, second) -> second).orElse(new Quiz()).
                getStudents().stream().flatMapToDouble(s -> DoubleStream.of(s.getGrade())).average().orElse(0);
        return new SessionDateAndStudentGrade(
                session.getStartDate(),
                session.getEndDate(), session.getId(),
                session.getCourse().getStudents().size() - session.getStudents().size(),
                grade
        );
    }
}
