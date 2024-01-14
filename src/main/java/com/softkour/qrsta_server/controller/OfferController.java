package com.softkour.qrsta_server.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/offer/")
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
            for (int i = 0; i < request.getCourseIds().size(); i++) {
                Offer offer = new Offer();
                offer.setCost(request.getCost());
                offer.setCourse(courseService.findOne(request.getCourseIds().get(i)));
                offer.setMonths(request.getMonths());
                offer.setEndDate(LocalDate.parse(request.getEndDate()));
                offerService.createOffer(offer);
            }
            return GenericResponse.successWithMessageOnly("offer create successfully");
        } catch (Exception e) {
            return GenericResponse.errorOfException(e);
        }
    }

    @GetMapping("all")
    public ResponseEntity<GenericResponse<Object>> getTeacherOffers() {

        return GenericResponse.success(offerService.userAvilableOffers()
                .stream().map((e) -> e.toOfferResponse()).toList());

    }

    @GetMapping("all")
    public ResponseEntity<GenericResponse<Object>> getStudentSubscripOffers() {

        return GenericResponse.success(offerService.studentSubscribedeOffers()
                .stream().map((e) -> e.toOfferResponse()).toList());

    }

}
