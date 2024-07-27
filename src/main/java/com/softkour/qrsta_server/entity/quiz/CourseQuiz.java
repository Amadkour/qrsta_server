package com.softkour.qrsta_server.entity.quiz;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.course.Course;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;

import com.softkour.qrsta_server.entity.user.Student;
import com.softkour.qrsta_server.payload.request.QuizCourseSession;
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<SessionQuiz> sessions = new HashSet<>();

    @Column()
    private Instant startDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"useOnlinePayment", "enableAbsence", "enableAutoJoin", "enableAutoChangeDevice"}, allowSetters = true)
    private Course course;

    public void addSession(SessionQuiz sessionQuiz) {
        sessions.add(sessionQuiz);
        sessionQuiz.setQuiz(this);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"students", "courses", "sessions", "students"}, allowSetters = true)
    private Quiz quiz;

    public QuizCourseSession toQuizCourseSession() {
        QuizCourseSession covered = new QuizCourseSession();
        if (getCourse() != null) {
            covered.setCourseId(getCourse().getId());
        }
        if (!getSessions().isEmpty()) {
            covered.setSessionsId(getSessions().stream().map(e -> e.getSession().getId()).toList());
        }
        return covered;

    }
}
