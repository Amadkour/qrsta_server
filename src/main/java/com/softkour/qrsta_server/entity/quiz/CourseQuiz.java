package com.softkour.qrsta_server.entity.quiz;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.course.Course;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
public class CourseQuiz extends AbstractAuditingEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "students", "quizes", "schedules" }, allowSetters = true)
    private Course course;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz", cascade = CascadeType.ALL)
    private Set<SessionQuiz> sessions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "students", "courses", "sessions", "students" }, allowSetters = true)
    private Quiz quiz;
}
