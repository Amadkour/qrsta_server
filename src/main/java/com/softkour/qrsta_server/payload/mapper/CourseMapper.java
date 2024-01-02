package com.softkour.qrsta_server.payload.mapper;

import com.softkour.qrsta_server.entity.Course;
import com.softkour.qrsta_server.payload.response.CourseResponse;
import org.mapstruct.factory.Mappers;

public class CourseMapper {
    public CourseResponse toDTOs(Course course) {
        return new CourseResponse(course.getId(),
                course.getName(),
                course.getStudents().size(),
                course.getSessions().size(),
                course.getCost(),
                course.getType()
        );
    }
}
