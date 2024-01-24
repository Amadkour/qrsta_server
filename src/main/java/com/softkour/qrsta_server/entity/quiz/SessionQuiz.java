package com.softkour.qrsta_server.entity.quiz;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.course.Session;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class SessionQuiz extends AbstractAuditingEntity {

    public SessionQuiz(Session session) {
        students = new HashSet<>();
        this.session = session;

    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz", cascade = CascadeType.ALL)
    private Set<StudentQuiz> students = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "students", "quizes", "schedules" }, allowSetters = true)
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    private CourseQuiz quiz;

    public SessionQuiz addStudent(StudentQuiz studentQuiz) {
        this.students.add(studentQuiz);
        return this;
    }

    public SessionQuiz removeStudent(StudentQuiz studentQuiz) {
        this.students.remove(studentQuiz);
        return this;
    }

}
