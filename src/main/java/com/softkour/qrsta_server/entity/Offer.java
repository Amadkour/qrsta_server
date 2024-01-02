package com.softkour.qrsta_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
//    @JsonIgnoreProperties(value = {"quizzes", "sessions", "schedules", "offers"}, allowSetters = true)
    private Course course;
}
