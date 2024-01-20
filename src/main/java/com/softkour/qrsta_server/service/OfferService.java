package com.softkour.qrsta_server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.Offer;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.repo.OfferRepo;

@Service
public class OfferService {
    @Autowired
    OfferRepo offerRepo;
    @Autowired
    AuthService authService;

    public Offer save(Offer offer) {
        return offerRepo.save(offer);
    }

    public Offer findOffer(Long offerId) {
        return offerRepo.findById(offerId).orElseThrow(() -> new ClientException("offer", "not found this offer"));
    }

    public List<Offer> userAvilableOffers() {
        User u = MyUtils.getCurrentUserSession(authService);
        if (u.getType() == UserType.TEACHER) {
            return teacherAllOffers(u.getId());
        } else {
            return studentAvailableOffers(u.getId());
        }
    }

    public List<Offer> teacherAllOffers(Long teacherId) {
        return offerRepo.findByCourses_teacher_id(teacherId);
    }

    public List<Offer> studentAvailableOffers(Long studentId) {
        return offerRepo.findBySoldoutAndCourses_students_id(false, studentId);
    }

    public List<Offer> studentSubscribedeOffers() {
        User u = MyUtils.getCurrentUserSession(authService);

        return offerRepo.findBySoldoutAndStudents_id(false, u.getId());
    }

    public Offer studentSubscribeToOffer(Long offerId) {
        User u = MyUtils.getCurrentUserSession(authService);
        Offer offer = offerRepo.findById(offerId)
                .orElseThrow(() -> new ClientException("offer", "Not Found this Offer: " + offerId));
        if (offer.isSoldout()) {
            throw new ClientException("offer", "This Offer is soldout: " + offerId);
        }
        offer.addStudent(u);
        return offerRepo.save(offer);
    }

}
