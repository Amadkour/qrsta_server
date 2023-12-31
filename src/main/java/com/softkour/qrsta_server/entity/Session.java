package com.softkour.qrsta_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Session.
 */
@Entity
@Getter
@Setter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Session extends AbstractAuditingEntity {


    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "sessions")
    @JsonIgnoreProperties(value = { "parent", "sessions", "courses" }, allowSetters = true)
    private Set<User> students = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "sessions")
    @JsonIgnoreProperties(value = { "sessions", "quizzes" }, allowSetters = true)
    private Set<Quiz> quizzes = new HashSet<>();

    @Column()
    private Instant startDate ;

    @Column()
    private Instant endDate ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sessions", "students", "schedules" }, allowSetters = true)
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

    public Session addStudent(User employee) {
        this.students.add(employee);
        employee.getSessions().add(this);
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

}
