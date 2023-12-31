package com.softkour.qrsta_server.service.mapper;

import com.softkour.qrsta_server.entity.Course;
import com.softkour.qrsta_server.service.dto.CourseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")

public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);
@Mapping(target = "sessionsCount", expression = "java(course.getSessions().size())")
@Mapping(target = "studentsCount", expression = "java(course.getStudents().size())")
@Mapping(target = "type",source = "type")
@Mapping(target = "name",source = "name")
CourseResponse toDTOs(Course course);
}
