package com.softkour.qrsta_server.entity.embedded_pk;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;

@Embeddable
@AllArgsConstructor
public class StudentCoursePK implements Serializable {

    @Column(name = "STUDENT_ID")
    private Long student_id;

    @Column(name = "COURSE_ID")
    private Long course_id;
}