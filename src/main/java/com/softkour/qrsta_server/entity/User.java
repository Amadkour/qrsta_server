package com.softkour.qrsta_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.enumeration.OrganizationType;
import com.softkour.qrsta_server.entity.enumeration.UserType;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Employee.
 */
@Entity
// @SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@Table(name = "qrsta_user")
public class User extends AbstractAuditingEntity<Long> {

    // private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
    // "sequenceGenerator")
    // @SequenceGenerator(name = "sequence_generator", sequenceName =
    // "sequence_generator", allocationSize = 1, schema = "public")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private UserType type;

    @Column(name = "organization")
    private OrganizationType organization;

    @NotNull
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    private String otp;

    @NotNull
    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Column(name = "mac_address")
    private String macAddress;

    @Column(name = "is_active", columnDefinition = "boolean default false")
    private boolean isActive;
    @Column(name = "is_loggen", columnDefinition = "boolean default false")
    private boolean isLoggen;

    @Column(name = "password")
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
    private LocalDateTime ExpireOTPDateTime;
    @Column
    private LocalDateTime ExpirePasswordDate;
}
