package com.softkour.qrsta_server.entity.quiz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;
import com.softkour.qrsta_server.entity.user.Student;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
public class StudentQuiz extends AbstractAuditingEntity {

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    private CourseQuiz course;
    @Column
    private double grade = 0;

    @Column(columnDefinition = "boolean default false")
    private boolean modified;
    @Column
    private String answers;

}
