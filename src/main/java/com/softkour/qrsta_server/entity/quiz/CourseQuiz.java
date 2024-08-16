package com.softkour.qrsta_server.entity.quiz;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.course.Course;
import com.softkour.qrsta_server.entity.course.Session;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;

import com.softkour.qrsta_server.entity.user.Student;
import com.softkour.qrsta_server.payload.request.QuizCourseSession;
import jakarta.persistence.*;
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

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "course_quiz_sessions",
            joinColumns = @JoinColumn(name = "course_quiz_id"),
            inverseJoinColumns = @JoinColumn(name = "sessions_id")
    )
    private Set<Session> sessions = new HashSet<>();

    @Column()
    private Instant startDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;

    public void addSession(Session session) {
        if (!this.sessions.contains(session)) {
            this.sessions.add(session);
            session.getCourseQuizzes().add(this);
        }
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Quiz quiz;

    public QuizCourseSession toQuizCourseSession() {
        QuizCourseSession covered = new QuizCourseSession();
        if (getCourse() != null) {
            covered.setCourseId(getCourse().getId());
        }
        if (!getSessions().isEmpty()) {
            covered.setSessionsId(getSessions().stream().map(AbstractAuditingEntity::getId).toList());
        }
        return covered;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<StudentQuiz> students = new HashSet<>();

}
