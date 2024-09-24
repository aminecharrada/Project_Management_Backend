package com.projectmanagement.ProjectManagement.controller;

import com.projectmanagement.ProjectManagement.service.KPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/kpi")
public class KPIController {

    @Autowired
    private KPIService kpiService;

    @GetMapping("/project/{projectId}/respect-delais")
    public Map<String, Double> getRespectDesDelais(@PathVariable Long projectId) {
        return kpiService.calculateRespectDesDelaisPerDate(projectId);
    }
    @GetMapping("/project/{projectId}/daily-progress")
    public Map<String, Double> getDailyProgress(@PathVariable Long projectId) {
        return kpiService.calculateDailyProgress(projectId);
    }
}

