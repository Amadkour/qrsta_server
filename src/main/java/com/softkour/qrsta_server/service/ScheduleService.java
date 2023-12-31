package com.softkour.qrsta_server.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softkour.qrsta_server.entity.Schedule;
import com.softkour.qrsta_server.repo.ScheduleRepository;

@Service
public class ScheduleService {

    private final Logger log = LoggerFactory.getLogger(ScheduleService.class);

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * Save a schedule.
     *
     * @param schedule the entity to save.
     * @return the persisted entity.
     */
    public Schedule save(Schedule schedule) {
        log.debug("Request to save Schedule : {}", schedule);
        return scheduleRepository.save(schedule);
    }

    /**
     * Update a schedule.
     *
     * @param schedule the entity to save.
     * @return the persisted entity.
     */
    public Schedule update(Schedule schedule) {
        log.debug("Request to update Schedule : {}", schedule);
        return scheduleRepository.save(schedule);
    }

    /**
     * Partially update a schedule.
     *
     * @param schedule the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Schedule> partialUpdate(Schedule schedule) {
        log.debug("Request to partially update Schedule : {}", schedule);

        return scheduleRepository
                .findById(schedule.getId())
                .map(existingSchedule -> {
                    if (schedule.getId() != null) {
                        existingSchedule.setId(schedule.getId());
                    }
                    if (schedule.getDay() != null) {
                        existingSchedule.setDay(schedule.getDay());
                    }
                    if (schedule.getFromTime() != null) {
                        existingSchedule.setFromTime(schedule.getFromTime());
                    }
                    if (schedule.getToTime() != null) {
                        existingSchedule.setToTime(schedule.getToTime());
                    }

                    return existingSchedule;
                })
                .map(scheduleRepository::save);
    }

    /**
     * Get all the schedules.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Schedule> findAll() {
        log.debug("Request to get all Schedules");
        return scheduleRepository.findAll();
    }

    /**
     * Get all the schedules with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Schedule> findAllWithEagerRelationships(Pageable pageable) {
        return scheduleRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one schedule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Schedule> findOne(Long id) {
        log.debug("Request to get Schedule : {}", id);
        return scheduleRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the schedule by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Schedule : {}", id);
        scheduleRepository.deleteById(id);
    }
}
