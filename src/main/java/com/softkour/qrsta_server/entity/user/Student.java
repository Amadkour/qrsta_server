package com.softkour.qrsta_server.entity.user;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.course.Offer;
import com.softkour.qrsta_server.entity.course.Session;
import com.softkour.qrsta_server.entity.course.StudentCourse;
import com.softkour.qrsta_server.entity.quiz.StudentQuiz;

import com.softkour.qrsta_server.payload.response.AbstractUser;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

/**
 * A Employee.
 */
@Entity
// @SuppressWarnings("common-java:DuplicatedBlocks")
@Setter
@Getter
public class Student extends AbstractAuditingEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"parent", "sessions", "courses"}, allowSetters = true)
    private User parent;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private User user;

    @Column(columnDefinition = "boolean default true")
    private boolean needToReplace;

    /// courses
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "student", cascade = CascadeType.ALL)
    private Set<StudentCourse> courses = new HashSet<>();

    public Set<StudentCourse> removeCourse(StudentCourse course) {
        courses.remove(course);
        return courses;
    }

    public Set<StudentCourse> addCourse(StudentCourse course) {
        courses.add(course);
        return courses;
    }

    /// sessions
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "students")
    @JsonIgnoreProperties(value = {"students", "quizzes", "course"}, allowSetters = true)
    private Set<Session> sessions = new HashSet<>();

    public Set<Session> addSession(Session session) {
        sessions.add(session);
        return sessions;
    }

    public Set<Session> removeSession(Session session) {
        sessions.remove(session);
        return sessions;
    }

    /// offers
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "students")
    @JsonIgnoreProperties(value = {"students", "courses"}, allowSetters = true)
    private Set<Offer> offers = new HashSet<>();

    /// quizzes

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.ALL)
    private Set<StudentQuiz> quizzes = new HashSet<>();


    public AbstractUser toAbstractUserWithDegree(double grade, String answers) {
        AbstractUser user = new AbstractUser(
                getId(),
                getUser().getName(),
                getUser().getType(),
                getUser().getImageUrl(),
                getUser().getPhoneNumber());
        user.setDegree(grade);
        user.setQuizAnswer(answers);
        return user;
    }
}