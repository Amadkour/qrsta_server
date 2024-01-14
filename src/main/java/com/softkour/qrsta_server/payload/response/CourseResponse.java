package com.softkour.qrsta_server.payload.response;

import java.util.List;

import com.softkour.qrsta_server.entity.enumeration.CourseType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourseResponse {
    private long id;
    private String name;
    private int studentsCount;
    private int sessionsCount;
    private double cost;
    private CourseType type;
    private List<ScheduleResponse> schedules;
}
