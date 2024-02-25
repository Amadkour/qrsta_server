package com.softkour.qrsta_server.entity.public_entity;

import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.payload.response.MyNotificationResponse;
import jakarta.persistence.CascadeType;
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
public class AppVersion extends AbstractAuditingEntity {
    @Column
    private int version;
    @Column
    private String appRelease;

    @Column(name = "is_ios")
    private boolean ios;

    @Column(name = "available")
    private boolean available;
}
