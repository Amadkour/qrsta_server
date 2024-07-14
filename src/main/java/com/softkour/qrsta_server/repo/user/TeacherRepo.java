package com.softkour.qrsta_server.repo.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softkour.qrsta_server.entity.user.Teacher;

public interface TeacherRepo extends JpaRepository<Teacher, Long> {

}
