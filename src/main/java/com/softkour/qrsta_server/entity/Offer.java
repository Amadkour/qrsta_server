package com.softkour.qrsta_server.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.payload.response.OfferResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Offer extends AbstractAuditingEntity {
    @Column
    private int months;
    @Column
    private double cost;
    @Column
    private boolean holdout;
    @Column
    private LocalDate endDate;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "offer__course", joinColumns = @JoinColumn(name = "offer_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Course course;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user__offer", joinColumns = @JoinColumn(name = "offer_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    @JsonIgnoreProperties(value = { "sessions", "courses" }, allowSetters = true)
    private Set<User> students = new HashSet<>();

    public OfferResponse toOfferResponse() {
        return new OfferResponse(this.getCourse().getName(), this.getMonths(), this.getCost(),
                this.getCourse().toCourseResponse());
    }
}
