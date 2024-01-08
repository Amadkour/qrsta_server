package com.softkour.qrsta_server.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SessionDetailsStudent {
    private List<StudntInSession> students;
}
