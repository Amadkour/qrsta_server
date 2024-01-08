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
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user__quiz", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "quiz_id"))
    @JsonIgnoreProperties(value = { "students", "sessions" }, allowSetters = true)
    private Set<Quiz> quizzes = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user__course", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    @JsonIgnoreProperties(value = { "sessions", "students", "schedules" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();
    @Column
    private Instant ExpireOTPDateTime;
    @Column
    private Instant ExpirePasswordDate;

    public Set<Session> removeSession(Session session) {
        sessions.remove(session);
        return sessions;
    }

    public Set<Session> addSession(Session session) {
        sessions.add(session);
        session.getStudents().add(this);
        return sessions;
    }

    public Set<Course> removeCourse(Course course) {
        courses.remove(course);
        return courses;
    }

    public Set<Course> addCourse(Course course) {
        courses.add(course);
        return courses;
    }

    public AbstractUser toAbstractUser() {
        return new AbstractUser(
                this.getId(), this.getName(), this.getType(), this.getImageUrl());
    }

    public StudntInSession toStudntInSession(boolean isPresent) {
        return new StudntInSession(
                this.getId(), this.getName(), this.getType(), this.getImageUrl(), isPresent
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
