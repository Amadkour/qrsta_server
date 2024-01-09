package com.softkour.qrsta_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.enumeration.QuizType;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Quiz.
 */
@Entity
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Quiz extends AbstractAuditingEntity {

    @Column()
    private Instant startDate;

    @Column()
    private Integer questionsPerStudent;

    @Enumerated(EnumType.STRING)
    @Column()
    private QuizType type;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "quiz__session", joinColumns = @JoinColumn(name = "quiz_id"), inverseJoinColumns = @JoinColumn(name = "session_id"))
    @JsonIgnoreProperties(value = { "students", "quizes", "course" }, allowSetters = true)
    private Set<Session> sessions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "quiz__course", joinColumns = @JoinColumn(name = "quiz_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    @JsonIgnoreProperties(value = { "students", "quizzes", "sessions", "schedules" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "quizzes")
    @JsonIgnoreProperties(value = { "options", "quizzes" }, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz", cascade = CascadeType.ALL)
    private Set<StudentQuiz> students = new HashSet<>();

    public void addSession(Session session) {
        this.sessions.add(session);
    }

    public void removeSession(Session session) {
        this.sessions.remove(session);
    }

    public void setQuestions(Set<Question> questions) {
        if (this.questions != null) {
            this.questions.forEach(i -> i.removeQuiz(this));
        }
        if (questions != null) {
            questions.forEach(i -> i.addQuiz(this));
        }
        this.questions = questions;
    }

    public Quiz addQuestion(Question question) {
        this.questions.add(question);
        question.getQuizzes().add(this);
        return this;
    }

    public Quiz removeQuestion(Question question) {
        this.questions.remove(question);
        question.getQuizzes().remove(this);
        return this;
    }

    public Quiz addStudent(StudentQuiz studentQuiz) {
        this.students.add(studentQuiz);
        return this;
    }

    public Quiz removeStudent(StudentQuiz studentQuiz) {
        this.students.remove(studentQuiz);
        return this;
    }
}
