package com.softkour.qrsta_server.service;

import com.softkour.qrsta_server.entity.Offer;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.repo.OfferRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferService {
    @Autowired
    OfferRepo offerRepo;

    public Offer createOffer(Offer offer) {
        return offerRepo.save(offer);
    }

    public List<Offer> getTeacherOffers(User u) {
        return offerRepo.findByCourse_teacher_id(u.getId());
    }

    public List<Offer> getStudentOffers(User u) {
        return offerRepo.findByCourse_students_id(u.getId());
    }
    public List<Offer> all() {
        return offerRepo.findAll();
    }

}
