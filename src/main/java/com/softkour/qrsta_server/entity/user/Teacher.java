package com.softkour.qrsta_server.entity.user;

import com.softkour.qrsta_server.entity.enumeration.OrganizationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

/**
 * A Employee.
 */
@Entity
// @SuppressWarnings("common-java:DuplicatedBlocks")
@Setter
@Getter
public class Teacher extends AbstractAuditingEntity {
    @Enumerated(EnumType.STRING)
    @Column()
    private OrganizationType organization;
    @Column
    private String paymentaccount;
    @Column(columnDefinition = "boolean default false")

    private boolean usePayment;
    @Column(columnDefinition = "boolean default false")
    private boolean enableAbsence;
    @Column(columnDefinition = "boolean default false")
    private boolean enableAutojoin;
    @Column(columnDefinition = "boolean default false")
    private boolean enableAutoChangeDevice;
}