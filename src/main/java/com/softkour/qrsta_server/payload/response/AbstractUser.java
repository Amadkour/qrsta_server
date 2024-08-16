package com.softkour.qrsta_server.payload.response;

import com.softkour.qrsta_server.entity.enumeration.UserType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class AbstractUser {
    private Long id;
    private String name;
    private UserType type;
    private String imageURL;
    private String phone;
    private String quizAnswer;
    private double degree;
    public AbstractUser(Long id, String name, UserType userType, String imageUrl, String phone){
        this.id=id;
        this.name=name;
        this.type=userType;
        this.imageURL=imageUrl;
        this.phone=phone;
    }

}
