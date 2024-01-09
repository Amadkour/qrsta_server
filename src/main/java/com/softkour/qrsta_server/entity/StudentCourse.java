package com.softkour.qrsta_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
public class StudentCourse extends AbstractAuditingEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "students", "quizes", "courses" }, allowSetters = true)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "students", "quizes", "quizzes", "schedules", "sessions" }, allowSetters = true)
    private Course course;

    @Column
    private int lateMonthes = 0;

}
