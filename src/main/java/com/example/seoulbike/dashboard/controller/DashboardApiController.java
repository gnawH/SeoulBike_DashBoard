package com.example.seoulbike.dashboard.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.seoulbike.auth.model.AuthResponse;
import com.example.seoulbike.dashboard.model.DashboardTrendPoint;
import com.example.seoulbike.dashboard.model.DashboardWordCloudItem;
import com.example.seoulbike.dashboard.service.DashboardWordCloudService;
import com.example.seoulbike.service.IDashboardService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApiController {

    private final DashboardWordCloudService wordCloudService;
    private final IDashboardService dashboardService;

    public DashboardApiController(DashboardWordCloudService wordCloudService, IDashboardService dashboardService) {
        this.wordCloudService = wordCloudService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/wordcloud")
    public List<DashboardWordCloudItem> getWordCloud(HttpSession session) {
        AuthResponse loginUser = (AuthResponse) session.getAttribute("loginUser");
        String region = (loginUser != null) ? loginUser.getRegion() : null;
        return wordCloudService.getWordCloudData(region);
    }

    @GetMapping("/trend")
    public List<DashboardTrendPoint> getTrend(@RequestParam("periodType") String periodType) {
        return dashboardService.getUsageTrend(periodType);
    }
}
