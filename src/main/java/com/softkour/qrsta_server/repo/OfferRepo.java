package com.softkour.qrsta_server.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softkour.qrsta_server.entity.Offer;

public interface OfferRepo extends JpaRepository<Offer, Long> {
    public List<Offer> findByCourse_teacher_id(Long id);

    public List<Offer> findBySoldoutAndStudents_id(boolean soldout, Long id);

    public List<Offer> findBySoldoutAndCourse_students_id(boolean soldout, Long id);
}
