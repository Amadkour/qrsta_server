package com.softkour.qrsta_server.entity.user;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.course.Session;
import com.softkour.qrsta_server.entity.enumeration.OrganizationType;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.entity.quiz.StudentCourse;
import com.softkour.qrsta_server.entity.quiz.StudentQuiz;
import com.softkour.qrsta_server.payload.response.AbstractUser;
import com.softkour.qrsta_server.payload.response.UserLoginResponse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Teacher extends User {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column()
    private OrganizationType organization;

    @Column(columnDefinition = "boolean default false")
    private boolean usePayment;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "students")
    @JsonIgnoreProperties(value = { "students", "quizzes", "course" }, allowSetters = true)
    private Set<Session> sessions = new HashSet<>();

    public Set<Session> removeSession(Session session) {
        sessions.remove(session);
        return sessions;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.ALL)
    private Set<StudentCourse> courses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.ALL)
    private Set<StudentQuiz> quizzes = new HashSet<>();

    public Set<StudentCourse> removeCourse(StudentCourse course) {
        courses.remove(course);
        return courses;
    }

    public Set<StudentCourse> addCourse(StudentCourse course) {
        courses.add(course);
        return courses;
    }

    public UserLoginResponse toUserLoginResponse() {
        return new UserLoginResponse(
                user.getName(),
                UserType.TEACHER,
                user.getPhoneNumber(),
                "token",
                user.getAddress(),
                user.getImageUrl(),
                (this.getOrganization() == null) ? null : this.getOrganization().name(),
                user.getNationalId(),
                user.getCountryCode(),
                user.getLoginMacAddress().equalsIgnoreCase(user.getRegisterMacAddress()),
                user.getDob());
    }

}