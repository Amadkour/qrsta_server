package com.softkour.qrsta_server.payload.response;

import com.softkour.qrsta_server.entity.SessionObject;
import com.softkour.qrsta_server.entity.User;

import java.time.Instant;
import java.util.Set;

public class SessionDetailsWithoutStudet {
    private long id;

    private Set<SessionObject> objects ;

    private Instant startDate ;

    private Instant endDate ;
}
