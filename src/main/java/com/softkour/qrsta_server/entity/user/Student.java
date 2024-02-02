package com.softkour.qrsta_server.entity.user;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.course.Offer;
import com.softkour.qrsta_server.entity.course.Session;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.entity.quiz.StudentCourse;
import com.softkour.qrsta_server.entity.quiz.StudentQuiz;
import com.softkour.qrsta_server.payload.response.AbstractChild;
import com.softkour.qrsta_server.payload.response.StudntInSession;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

/**
 * A Employee.
 */
@Entity
@Setter
@Getter
public class Student extends User {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private Parent parent;

    @Column(columnDefinition = "boolean default true")
    private boolean needToReplace;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "students")
    @JsonIgnoreProperties(value = { "students", "quizzes", "course" }, allowSetters = true)
    private Set<Session> sessions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "students")
    @JsonIgnoreProperties(value = { "students", "courses" }, allowSetters = true)
    private Set<Offer> offers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.ALL)
    private Set<StudentCourse> courses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.ALL)
    private Set<StudentQuiz> quizzes = new HashSet<>();

    public Set<Session> addSession(Session session) {
        sessions.add(session);
        session.getStudents().add(this);
        return sessions;
    }

    public AbstractChild toAbstractChild() {
        return new AbstractChild(
                this.getId(), this.getName(), this.getType(), this.getImageUrl(), getPhoneNumber(),
                getCourses().stream().dropWhile(e -> !e.isActive()).count());
    }

    public Set<Session> removeSession(Session session) {
        sessions.remove(session);
        return sessions;
    }

    public Set<StudentCourse> removeCourse(StudentCourse course) {
        courses.remove(course);
        return courses;
    }

    public Set<StudentCourse> addCourse(StudentCourse course) {
        courses.add(course);
        return courses;
    }

    public StudntInSession toStudntInSession(List<Boolean> attendance, boolean isPresent, Long courseId) {
        StudentCourse studentCourse = this.getCourses().stream().filter(e -> e.getCourse().getId() == courseId).toList()
                .get(0);
        Stream<StudentQuiz> studentQuizzes = this.getQuizzes().stream()
                .filter(q -> q.getQuiz().getQuiz().getCourse().getId() == studentCourse.getCourse().getId());
        List<Instant> dInstants = studentCourse.getCourse().getSessions().stream().map(s -> s.getCreatedDate())
                .toList();
        int firstIndex = 0;
        for (int i = 0; i < dInstants.size(); i++) {
            if (dInstants.get(i).isBefore(this.getCreatedDate()))
                firstIndex = i;
        }
        return new StudntInSession(
                this.getId(),
                getName(),
                getAddress(),
                UserType.STUDENT,
                getImageUrl(),
                attendance,
                isPresent,
                studentCourse.getLate(),
                studentCourse.isActive(),
                studentQuizzes.mapToDouble(e -> e.getGrade()).sum(),
                firstIndex + 1

        );
    }

}