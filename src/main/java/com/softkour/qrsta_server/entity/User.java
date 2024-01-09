package com.softkour.qrsta_server.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.enumeration.DeviceType;
import com.softkour.qrsta_server.entity.enumeration.OrganizationType;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.payload.response.AbstractUser;
import com.softkour.qrsta_server.payload.response.StudntInSession;
import com.softkour.qrsta_server.payload.response.UserLoginResponse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * A Employee.
 */
@Entity
// @SuppressWarnings("common-java:DuplicatedBlocks")
@Setter
@Getter
@Table(name = "qrsta_user")
public class User extends AbstractAuditingEntity {

    // private static final long serialVersionUID = 1L;
    @NotNull
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column()
    private UserType type;

    @Enumerated(EnumType.STRING)
    @Column()
    private OrganizationType organization;

    @NotNull
    @Column(nullable = false, unique = true)
    @Size(max = 10, min = 7)
    // @Pattern(regexp = Constants.PHONE_REGEX)
    private String phoneNumber;

    @Column(nullable = true, unique = true)
    @Size(max = 14, min = 9)
    private String nationalId;

    private String otp;
    @NotNull
    private String countryCode;

    @Column()
    private LocalDate dob;

    @Column()
    private String macAddress;

    @Column()
    private String imageUrl;

    @Column()
    private String address;

    @Column(columnDefinition = "boolean default false")
    private boolean isActive;
    @Column(columnDefinition = "boolean default false")
    private boolean isLogged;

    @Column(columnDefinition = "integer default 0")
    private int logoutTimes;

    @Column()
    private String password;
    @Column()
    private String fcmToken;
    @Column()
    private DeviceType deviceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent", "sessions", "courses" }, allowSetters = true)
    private User parent;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "students")
    @JsonIgnoreProperties(value = { "students", "quizzes", "course" }, allowSetters = true)
    private Set<Session> sessions = new HashSet<>();

    @Column
    private Instant ExpireOTPDateTime;
    @Column
    private Instant ExpirePasswordDate;

    public Set<Session> removeSession(Session session) {
        sessions.remove(session);
        return sessions;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.ALL)
    private Set<StudentCourse> courses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.ALL)
    private Set<StudentQuiz> quizzes = new HashSet<>();

    public Set<Session> addSession(Session session) {
        sessions.add(session);
        session.getStudents().add(this);
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

    public AbstractUser toAbstractUser() {
        return new AbstractUser(
                this.getId(), this.getName(), this.getType(), this.getImageUrl());
    }

    public StudntInSession toStudntInSession(boolean isPresent, Long courseId) {
        return new StudntInSession(
                this.getId(), this.getName(), this.getType(), this.getImageUrl(), isPresent, this.getCourses().stream()
                        .filter((c) -> c.getCourse().getId() == courseId).findFirst().get().getLateMonthes(),
                this.getQuizzes().stream()
                        .filter(e -> e.getQuiz().getCourses().stream().anyMatch(c -> c.getId() == courseId))
                        .reduce(0.0, (a, b) -> a + b.getGrade(), Double::sum)
        // this.getSessions(),
        // this.getCourses(),
        // this.getQuizzes()
        );
    }

    public UserLoginResponse toUserLoginResponse() {
        return new UserLoginResponse(
                this.getName(),
                this.getType(),
                this.getPhoneNumber(),
                "token",
                this.getAddress(),
                this.getMacAddress(),
                this.getImageUrl(),
                (this.getOrganization() == null) ? null : this.getOrganization().name(),
                this.getNationalId(),
                this.getCountryCode());
    }
}
