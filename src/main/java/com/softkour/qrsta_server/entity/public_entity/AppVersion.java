package com.softkour.qrsta_server.entity.public_entity;

import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class AppVersion extends AbstractAuditingEntity {
    @Column
    private String version;
    @Column
    private String appRelease;

    @Column(name = "is_ios")
    private boolean ios;

    @Column(name = "available")
    private boolean available;
}
