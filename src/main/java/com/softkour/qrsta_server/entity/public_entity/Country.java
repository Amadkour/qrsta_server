
package com.softkour.qrsta_server.entity.public_entity;

import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;
import com.softkour.qrsta_server.payload.response.CountryResponce;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Country extends AbstractAuditingEntity {
    private String name;
    private String imageUrl;
    private String phoneCode;
    private String sidMax;
    private String sidMin;
    private String phoneLength;
    private String phoneStart;

    public CountryResponce toCountryResponce() {
        return new CountryResponce(
                getName(),
                getImageUrl(),
                getPhoneCode(),
                getSidMax(),
                getSidMin(),
                getPhoneLength(),
                getPhoneStart());
    }

}
