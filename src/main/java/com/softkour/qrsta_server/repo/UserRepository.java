package com.softkour.qrsta_server.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   public User findUserByPhoneNumber(String phone);

   public List<User> findAllUserByStudent_needToReplaceAndCourses_course_teacherId(boolean needToReplace,
         Long teacherId);

   public List<User> findAllChildrenByStudent_parent_id(Long parentId);

   public boolean existsByPhoneNumberAndIsActive(String phone, boolean active);

   public boolean existsByNationalId(String nationalId);

   public User getScoreByIdAndQuizzes_quiz_session_course_id(Long userId, Long courseId);

   public User getScoreByIdAndCourses_course_id(Long userId, Long courseId);

   public List<User> getStudentByStudent_parent_password(String password);

   public List<User> getStudentByCourses_course_teacher_id(Long teacherId);

}
