package com.softkour.qrsta_server.repo;

import com.softkour.qrsta_server.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfferRepo extends JpaRepository<Offer,Long> {
    public List<Offer> findByCourse_teacher_id(Long id);
    public List<Offer> findByCourse_students_id(Long id);
}
