package com.softkour.qrsta_server.repo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.softkour.qrsta_server.entity.Schedule;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class ScheduleRepositoryWithBagRelationshipsImpl implements ScheduleRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Schedule> fetchBagRelationships(Optional<Schedule> schedule) {
        return schedule.map(this::fetchCourses);
    }

    @Override
    public Page<Schedule> fetchBagRelationships(Page<Schedule> schedules) {
        return new PageImpl<>(fetchBagRelationships(schedules.getContent()), schedules.getPageable(),
                schedules.getTotalElements());
    }

    @Override
    public List<Schedule> fetchBagRelationships(List<Schedule> schedules) {
        return Optional.of(schedules).map(this::fetchCourses).orElse(Collections.emptyList());
    }

    Schedule fetchCourses(Schedule result) {
        return entityManager
                .createQuery(
                        "select schedule from Schedule schedule left join fetch schedule.courses where schedule.id = :id",
                        Schedule.class)
                .setParameter("id", result.getId())
                .getSingleResult();
    }

    List<Schedule> fetchCourses(List<Schedule> schedules) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, schedules.size()).forEach(index -> order.put(schedules.get(index).getId(), index));
        List<Schedule> result = entityManager
                .createQuery(
                        "select schedule from Schedule schedule left join fetch schedule.courses where schedule in :schedules",
                        Schedule.class)
                .setParameter("schedules", schedules)
                .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
