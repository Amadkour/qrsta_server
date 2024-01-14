package com.softkour.qrsta_server.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.Schedule;

/**
 * Spring Data JPA repository for the Schedule entity.
 *
 * When extending this class, extend ScheduleRepositoryWithBagRelationships too.
 * For more information refer to
 * https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ScheduleRepository extends ScheduleRepositoryWithBagRelationships, JpaRepository<Schedule, Long> {
    default Optional<Schedule> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Schedule> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Schedule> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    List<Schedule> findAllByCourses_teacher_Id(Long userId);

    List<Schedule> findAllByCourses_students_student_Id(Long userId);
}
