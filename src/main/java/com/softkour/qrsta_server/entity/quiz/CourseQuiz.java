package com.softkour.qrsta_server.entity.quiz;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseQuiz extends AbstractAuditingEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz", cascade = CascadeType.PERSIST)
    private Set<SessionQuiz> sessions = new HashSet<>();
    @Column()
    private Instant startDate;

    public void addSession(SessionQuiz sessionQuiz) {
        sessions.add(sessionQuiz);
        sessionQuiz.setQuiz(this);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"students", "courses", "sessions", "students"}, allowSetters = true)
    private Quiz quiz;
}
