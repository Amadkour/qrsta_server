package com.softkour.qrsta_server.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.User;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   @Query("select count(u) = 1 from User u where :key = :value")
   public boolean isExist(@Param("key") String key, @Param("value") String value);

   public User findUserByPhoneNumber(String phone);

   public boolean existsByPhoneNumber(String phone);

   public boolean existsByNationalId(String nationalId);
}
