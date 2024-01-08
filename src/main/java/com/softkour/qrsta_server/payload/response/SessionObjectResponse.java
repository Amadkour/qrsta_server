package com.softkour.qrsta_server.payload.response;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.softkour.qrsta_server.entity.enumeration.SessionObjectType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SessionObjectResponse {

    private String title;

    private List<SessionObjectResponse> subItems = new ArrayList<>();

    private SessionObjectType type;
    private Instant createDate;
    private long id;

}
