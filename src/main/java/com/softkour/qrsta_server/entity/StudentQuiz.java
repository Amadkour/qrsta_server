package com.softkour.qrsta_server.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    @JsonIgnoreProperties(value = { "students", "courses", "sessions", "students" }, allowSetters = true)
    private Quiz quiz;

    private double grade = 0;
}
