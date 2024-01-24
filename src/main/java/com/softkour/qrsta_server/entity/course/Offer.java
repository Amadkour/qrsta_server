package com.softkour.qrsta_server.entity.course;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.payload.response.OfferResponse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Offer extends AbstractAuditingEntity {
    @Column
    private int months;
    @Column
    private String cost;
    @Column
    private boolean soldout;
    @Column
    private Instant endDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "course__offer", joinColumns = @JoinColumn(name = "offer_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    @JsonIgnoreProperties(value = { "sessions", "courses" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user__offer", joinColumns = @JoinColumn(name = "offer_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    @JsonIgnoreProperties(value = { "sessions", "courses" }, allowSetters = true)
    private Set<User> students = new HashSet<>();

    public OfferResponse toOfferResponse() {
        return new OfferResponse(getId(), getCourses().stream().map(e -> e.toCourseResponse()).toList(), getMonths(),
                getCost(), getEndDate(), isSoldout());
    }

    public void addStudent(User user) {
        students.add(user);
    }
}
