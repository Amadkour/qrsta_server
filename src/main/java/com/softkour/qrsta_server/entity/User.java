package com.softkour.qrsta_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.config.Constants;
import com.softkour.qrsta_server.entity.enumeration.OrganizationType;
import com.softkour.qrsta_server.entity.enumeration.UserType;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    @Column( nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column()
    private UserType type;

    @Enumerated(EnumType.STRING)
    @Column()
    private OrganizationType organization;

    @NotNull
    @Column( nullable = false, unique = true)
    @Size(max = 11,min = 9)
//    @Pattern(regexp = Constants.PHONE_REGEX)
    private String phoneNumber;

    @NotNull
    @Column( nullable = false, unique = true)
    @Size(max = 14,min = 9)
    private String nationalId;

    private String otp;

    @Column()
    private LocalDate dob;

    @Column()
    private String macAddress;

    @Column()
    private String imageUrl;

    @Column()
    private String address;

    @Column( columnDefinition = "boolean default false")
    private boolean isActive;
    @Column( columnDefinition = "boolean default false")
    private boolean isLogged;

    @Column(columnDefinition = "integer default 0")
    private int logoutTimes;

    @Column()
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent", "sessions", "courses" }, allowSetters = true)
    private User parent;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "students")
    @JsonIgnoreProperties(value = {"students", "quizzes", "course" }, allowSetters = true)
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

    public Set<Session> removeSession(Session session){
        sessions.remove(session);
        return  sessions;
    }
    public Set<Session> addSession(Session session){
        sessions.add(session);
        session.getStudents().add(this);
        return  sessions;
    }

    public Set<Course> removeCourse(Course course){
        courses.remove(course);
        return  courses;
    }
    public Set<Course> addCourse(Course course){
        courses.add(course);
        return  courses;
    }
}
