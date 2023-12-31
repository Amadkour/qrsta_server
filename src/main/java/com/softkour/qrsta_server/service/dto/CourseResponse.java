package com.softkour.qrsta_server.service.dto;

import com.softkour.qrsta_server.entity.enumeration.CourseType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseResponse {
    private long id;
    private String name;
    private int studentsCount;
    private int sessionsCount;
    private double cost;
    private CourseType type;
}
