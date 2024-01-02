package com.softkour.qrsta_server.payload.mapper;

import com.softkour.qrsta_server.entity.Offer;
import com.softkour.qrsta_server.payload.response.OfferResponse;

public class OfferMapper {

    public OfferResponse toDto(Offer offer) {
        return new OfferResponse(offer.getCourse().getName(), offer.getMonths(), offer.getCost(),new CourseMapper().toDTOs( offer.getCourse()));
    }
}
