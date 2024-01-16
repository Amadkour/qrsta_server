package com.softkour.qrsta_server.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   public User findUserByPhoneNumber(String phone);

   public List<User> findAllUserByNeedToReplaceAndCourses_course_teacherId(boolean needToReplace, Long teacherId);

   public boolean existsByPhoneNumber(String phone);

   public boolean existsByNationalId(String nationalId);
}
