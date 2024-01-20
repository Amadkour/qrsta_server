package com.softkour.qrsta_server.controller;

import java.time.Instant;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.entity.Offer;
import com.softkour.qrsta_server.payload.request.OfferCreationRequest;
import com.softkour.qrsta_server.service.AuthService;
import com.softkour.qrsta_server.service.CourseService;
import com.softkour.qrsta_server.service.OfferService;

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/api/offer/")
public class OfferController {
    @Autowired
    CourseService courseService;
    @Autowired
    OfferService offerService;
    @Autowired
    AuthService authService;

    @PostMapping("create")
    public ResponseEntity<GenericResponse<Object>> createOffer(@RequestBody @Valid OfferCreationRequest request) {
        try {
            Offer offer = new Offer();
            for (int i = 0; i < request.getCourseIds().size(); i++) {
                offer.setCost(request.getCost());
                offer.setCourses(new HashSet<>(request.getCourseIds().stream().map(e -> courseService
                        .findOne(e)).toList()));
                offer.setMonths(request.getMonths());
                offer.setEndDate(Instant.parse(request.getEndDate()));
                offer = offerService.save(offer);
            }
            return GenericResponse.success(offer.toOfferResponse());
        } catch (Exception e) {
            return GenericResponse.errorOfException(e);
        }
    }

    @GetMapping("all")
    public ResponseEntity<GenericResponse<Object>> getTeacherOffers() {

        return GenericResponse.success(offerService.userAvilableOffers()
                .stream().map((e) -> e.toOfferResponse()).toList());

    }

    @GetMapping("subscribed-offers")
    public ResponseEntity<GenericResponse<Object>> getStudentSubscribedOffers() {

        return GenericResponse.success(offerService.studentSubscribedeOffers()
                .stream().map((e) -> e.toOfferResponse()).toList());

    }

    @GetMapping("subscribe")
    public ResponseEntity<GenericResponse<Object>> getStudentSubscribeInOffers(
            @RequestHeader("offer_id") Long offerId) {

        return GenericResponse.success(offerService.studentSubscribeToOffer(offerId).toOfferResponse());

    }

    @GetMapping("change_status")
    public ResponseEntity<GenericResponse<Object>> changeStatus(
            @RequestHeader("offer_id") Long offerId, @RequestHeader("offer_status") boolean status) {
        Offer offer = offerService.findOffer(offerId);
        offer.setSoldout(status);
        return GenericResponse.success(offerService.save(offer).toOfferResponse());

    }

}
