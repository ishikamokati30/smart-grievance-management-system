package com.ishika.grievance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ishika.grievance.dto.DashboardResponse;
import com.ishika.grievance.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public DashboardResponse getStats() {
        return dashboardService.getStats();
    }
}