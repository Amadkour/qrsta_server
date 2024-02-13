package com.softkour.qrsta_server.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SessionNameAndId {
    private Long sessionId;
    private String label;

}
