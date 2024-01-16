package com.softkour.qrsta_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta_server.service.ScheduleService;

@RestController
@RequestMapping("api/shedule/")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @GetMapping("all")
    void getShedule() {

        scheduleService.userSchedule();

    }

}
