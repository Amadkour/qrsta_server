package com.softkour.qrsta_server.payload.mapper;

import com.softkour.qrsta_server.entity.Course;
import com.softkour.qrsta_server.payload.response.CourseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")

public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);
//@Mapping(target = "sessionsCount", expression = "java(course.getSessions().size())")
//@Mapping(target = "studentsCount", expression = "java(course.getStudents().size())")
CourseResponse toDTOs(Course course);
}
