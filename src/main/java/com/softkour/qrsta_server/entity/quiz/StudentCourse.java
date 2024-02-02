package com.softkour.qrsta_server.entity.quiz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.course.Course;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;
import com.softkour.qrsta_server.entity.user.Student;
import com.softkour.qrsta_server.entity.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class StudentCourse extends AbstractAuditingEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "students", "quizes", "courses" }, allowSetters = true)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "students", "quizes", "quizzes", "schedules", "sessions" }, allowSetters = true)
    private Course course;

    @Column
    private int late = 0;
    @Column
    private boolean active = false;
    @Column
    private boolean finished = true;

}
