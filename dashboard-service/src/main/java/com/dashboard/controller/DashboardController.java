package com.dashboard.controller;

import com.dashboard.dto.DashboardResponse;
import com.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard(){
        DashboardResponse dashboardResponse = dashboardService.getDashboard();
        return null;
    }
}
