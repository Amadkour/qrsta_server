package com.softkour.qrsta_server.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.payload.response.AbstractChild;
import com.softkour.qrsta_server.payload.response.AbstractUser;
import com.softkour.qrsta_server.payload.response.UserLoginResponse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * A Employee.
 */
@Entity
// @SuppressWarnings("common-java:DuplicatedBlocks")
@Setter
@Getter
public class Parent extends User {

}