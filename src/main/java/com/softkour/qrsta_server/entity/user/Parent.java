package com.softkour.qrsta_server.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * A Employee.
 */
@Entity
// @SuppressWarnings("common-java:DuplicatedBlocks")
@Setter
@Getter
public class Parent extends AbstractAuditingEntity {
    @Column
    private int late = 0;

}