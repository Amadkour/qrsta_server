package com.softkour.qrsta_server.entity.quiz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;
import com.softkour.qrsta_server.entity.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class StudentQuiz extends AbstractAuditingEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "students", "quizes", "courses" }, allowSetters = true)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    private SessionQuiz quiz;

    private double grade = 0;
}
