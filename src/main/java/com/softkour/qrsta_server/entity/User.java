package com.softkour.qrsta_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.enumeration.OrganizationType;
import com.softkour.qrsta_server.entity.enumeration.UserType;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
public class User extends AbstractAuditingEntity<Long> {

    // private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotNull
    @Column( nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column()
    private UserType type;

    @Column()
    private OrganizationType organization;

    @NotNull
    @Column( nullable = false, unique = true)
    private String phoneNumber;

    private String otp;

    @NotNull
    @Column( nullable = false)
    private LocalDate dob;

    @Column()
    private String macAddress;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user__session", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "session_id"))
    @JsonIgnoreProperties(value = { "students", "quizes", "course" }, allowSetters = true)
    private Set<Session> sessions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user__course", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    @JsonIgnoreProperties(value = { "sessions", "students", "schedules" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();
    /// ==========================
    @Column
    private Instant ExpireOTPDateTime;
    @Column
    private Instant ExpirePasswordDate;
}
