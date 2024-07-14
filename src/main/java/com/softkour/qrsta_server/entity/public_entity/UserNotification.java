package com.softkour.qrsta_server.entity.public_entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;
import com.softkour.qrsta_server.entity.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class UserNotification extends AbstractAuditingEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sessions", "courses", "teacher", "student", "parent",
            "quizzes" }, allowSetters = true)
    private User user;
    @Column(name = "read")
    private boolean read;

}
