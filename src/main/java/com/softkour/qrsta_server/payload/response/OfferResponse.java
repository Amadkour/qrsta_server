package com.softkour.qrsta_server.payload.response;

import com.softkour.qrsta_server.entity.Course;
import com.softkour.qrsta_server.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class OfferResponse {
    private String CourseName;
    private int months;
    private double cost;
    //    private Set<UserLogo> students;
    private CourseResponse course;
}
