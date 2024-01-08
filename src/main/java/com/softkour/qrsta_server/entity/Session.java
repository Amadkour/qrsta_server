package com.softkour.qrsta_server.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.DoubleStream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.payload.response.SessionDateAndStudentGrade;
import com.softkour.qrsta_server.payload.response.SessionDetailsStudent;
import com.softkour.qrsta_server.payload.response.SessionDetailsWithoutStudents;
import com.softkour.qrsta_server.payload.response.SessionObjectResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * A Session.
 */
@Entity
@Getter
@Setter
@Slf4j
public class Session extends AbstractAuditingEntity {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user__session", joinColumns = @JoinColumn(name = "session_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    @JsonIgnoreProperties(value = { "sessions", "courses" }, allowSetters = true)
    private Set<User> students = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "sessions")
    @JsonIgnoreProperties(value = { "sessions", "quizzes" }, allowSetters = true)
    private Set<Quiz> quizzes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "session")
    @JsonIgnoreProperties(value = { "session" }, allowSetters = true)
    private Set<SessionObject> objects = new HashSet<>();

    @Column()
    private Instant startDate;

    @Column()
    private Instant endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sessions", "schedules" }, allowSetters = true)
    private Course course;

    public void setStudents(Set<User> students) {
        if (this.students != null) {
            this.students.forEach(i -> i.removeSession(this));
        }
        if (students != null) {
            students.forEach(i -> i.addSession(this));
        }
        this.students = students;
    }

    public Session addStudent(User student) {
        this.students.add(student);
        student.getSessions().add(this);
        return this;
    }

    public Session removeStudent(User employee) {
        this.students.remove(employee);
        employee.getSessions().remove(this);
        return this;
    }

    public void setQuizzes(Set<Quiz> quizzes) {
        if (this.quizzes != null) {
            this.quizzes.forEach(i -> i.removeSession(this));
        }
        if (quizzes != null) {
            quizzes.forEach(i -> i.addSession(this));
        }
        this.quizzes = quizzes;
    }

    public Session addQuiz(Quiz quiz) {
        this.quizzes.add(quiz);
        quiz.getSessions().add(this);
        return this;
    }

    public Session removeQuiz(Quiz quiz) {
        this.quizzes.remove(quiz);
        quiz.getSessions().remove(this);
        return this;
    }

    public SessionDateAndStudentGrade toSessionDateAndStudentGrade() {
        double grade = this.getQuizzes().stream().reduce((first, second) -> second).orElse(new Quiz()).getStudents()
                .stream().flatMapToDouble(s -> DoubleStream.of(s.getGrade())).average().orElse(0);
        return new SessionDateAndStudentGrade(
                this.getStartDate(),
                this.getEndDate(), this.getId(),
                this.getStudents().size(),
                this.getCourse().getStudents().size(),
                grade);
    }

    public SessionDetailsStudent toSessionDetailsStudent() {

        log.warn(String.valueOf(this.getCourse().getStudents().size()));
        log.warn(String.valueOf(this.getCourse().getId()));
        log.warn(String.valueOf(this.getStudents().size()));
        // this.getCourse().getStudents().stream()
        // .map((e) -> e.getStudent()
        // .toStudntInSession(
        // this.getStudents().stream().anyMatch((s) -> s.getId() ==
        // e.getStudent().getId())))
        // .toList()
        // this.getCourse().getStudents().stream().map(
        // (c) -> c.toStudntInSession(this.getStudents().stream().anyMatch((s) ->
        // s.getId() == c.getId())))
        // .collect(Collectors.toSet());
        return new SessionDetailsStudent(this.getStudents().stream().map((e) -> e.toStudntInSession(false)).toList());

    }

    public SessionDetailsWithoutStudents toSessionDetailsWithoutStudents() {
        return new SessionDetailsWithoutStudents(
                this.getObjects().stream()
                        .map((e) -> new SessionObjectResponse(e.getTitle(),
                                e.getSubItems().stream().map((s) -> new SessionObjectResponse(s.getTitle(),
                                        null, s.getType(), s.getCreatedDate(), s.getId())).toList(),
                                e.getType(), e.getCreatedDate(), e.getId()))
                        .toList());
    }
}
