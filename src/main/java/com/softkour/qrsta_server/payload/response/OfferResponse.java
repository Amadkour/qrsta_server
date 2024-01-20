package com.softkour.qrsta_server.payload.response;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class OfferResponse {
    private Long offerId;
    private List<CourseResponse> courses;
    private int months;
    private String cost;
    private Instant endDate;
    private boolean soldout;
    // private Set<UserLogo> students;
    // private CourseResponse course;
}
