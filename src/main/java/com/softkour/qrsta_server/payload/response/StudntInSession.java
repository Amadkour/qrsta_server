package com.softkour.qrsta_server.payload.response;

import java.util.List;

import com.softkour.qrsta_server.entity.enumeration.UserType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudntInSession {
    private Long id;
    private String name;
    private String address;
    private UserType type;
    private String imageURL;
    private List<Boolean> attendance;
    private boolean isPresented;
    private int late;
    private boolean active;
    private double grade;
    private int firstSession;
}
