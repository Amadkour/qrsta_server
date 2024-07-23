package com.softkour.qrsta_server.config.AI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String role;
    private String content;// prompt,it will carry your message
}