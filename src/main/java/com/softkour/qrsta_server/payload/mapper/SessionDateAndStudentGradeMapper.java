package com.softkour.qrsta_server.payload.mapper;

import com.softkour.qrsta_server.entity.Session;
import com.softkour.qrsta_server.payload.response.SessionDateAndStudentGrade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SessionDateAndStudentGradeMapper {
    SessionDateAndStudentGradeMapper INSTANCE= Mappers.getMapper(SessionDateAndStudentGradeMapper.class);

//    @Mapping(target = "attendance_count",expression = "java(session.getCourse().getStudents().size() - session.getStudents().size())")
    SessionDateAndStudentGrade toDto(Session session);
}
