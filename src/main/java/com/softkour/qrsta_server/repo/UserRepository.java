package com.softkour.qrsta_server.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   public User findUserByPhoneNumber(String phone);

   public List<User> findAllUserByNeedToReplaceAndCourses_course_teacherId(boolean needToReplace, Long teacherId);

   public List<User> findAllChildrenByParent_id(Long parentId);

   public boolean existsByPhoneNumber(String phone);

   public boolean existsByNationalId(String nationalId);

   public User getScoreByIdAndQuizzes_quiz_session_course_id(Long userId, Long courseId);

   public User getScoreByIdAndCourses_course_id(Long userId, Long courseId);
}
