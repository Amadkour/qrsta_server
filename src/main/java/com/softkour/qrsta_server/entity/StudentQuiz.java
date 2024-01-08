package com.softkour.qrsta_server.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class StudentQuiz extends AbstractAuditingEntity {
    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sessions", "quizzes", "courses", "teacher" }, allowSetters = true)
    private Set<User> students = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "o__course", joinColumns = @JoinColumn(name = "offer_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    @JsonIgnoreProperties(value = { "sessions", "students", "courses" }, allowSetters = true)
    private Set<Quiz> quizzes = new HashSet<>();
    @Column
    private double grade;
}
