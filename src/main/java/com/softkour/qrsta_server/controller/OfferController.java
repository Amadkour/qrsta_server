package com.softkour.qrsta_server.controller;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.Offer;
import com.softkour.qrsta_server.payload.mapper.OfferMapper;
import com.softkour.qrsta_server.payload.request.OfferCreationRequest;
import com.softkour.qrsta_server.payload.response.OfferResponse;
import com.softkour.qrsta_server.repo.UserRepository;
import com.softkour.qrsta_server.service.CourseService;
import com.softkour.qrsta_server.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@Validated
@RequestMapping("/offer/")
public class OfferController {
    @Autowired
    CourseService courseService;
    @Autowired
    OfferService offerService;
    @Autowired
    UserRepository userRepository;

    @PostMapping("create")
    public ResponseEntity<GenericResponse<String>> createOffer(@RequestBody @Valid OfferCreationRequest request) {
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
            return GenericResponse.error(e.getMessage());
        }
    }
    @Cacheable(value = "offers")
    @GetMapping("teacher_offers")
    public ResponseEntity<GenericResponse<Object>> getTeacherOffers() {
        try {
            return GenericResponse.success(offerService.getTeacherOffers(MyUtils.getCurrentUserSession(userRepository)).stream().map(new OfferMapper()::toDto).collect(Collectors.toSet()));
        } catch (Exception e) {
            return GenericResponse.error(e.getMessage());
        }
    }

    @GetMapping("student_offers")
    public ResponseEntity<GenericResponse<Object>> getStudentOffers() {
        try {
            return GenericResponse.success(offerService.getStudentOffers(MyUtils.getCurrentUserSession(userRepository)).stream().map(new OfferMapper()::toDto).collect(Collectors.toSet()));
        } catch (Exception e) {
            return GenericResponse.error(e.getMessage());
        }
    }

    @GetMapping("all")
    public ResponseEntity<GenericResponse<Object>> all() {
        try {
            return GenericResponse.success(offerService.all().stream().map(new OfferMapper()::toDto).collect(Collectors.toSet()));
        } catch (Exception e) {
            return GenericResponse.error(e.getMessage());
        }
    }

}
