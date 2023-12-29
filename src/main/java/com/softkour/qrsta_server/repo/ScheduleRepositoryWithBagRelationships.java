package com.softkour.qrsta_server.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

import com.softkour.qrsta_server.entity.Schedule;

public interface ScheduleRepositoryWithBagRelationships {
    Optional<Schedule> fetchBagRelationships(Optional<Schedule> schedule);

    List<Schedule> fetchBagRelationships(List<Schedule> schedules);

    Page<Schedule> fetchBagRelationships(Page<Schedule> schedules);
}
