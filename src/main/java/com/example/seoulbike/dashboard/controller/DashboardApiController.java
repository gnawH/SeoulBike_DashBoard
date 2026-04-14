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

    private final IDashboardService dashboardService;
    private final DashboardWordCloudService wordCloudService;
    private final com.example.seoulbike.dashboard.service.DashboardRealtimeMapService realtimeMapService;

    public DashboardApiController(DashboardWordCloudService wordCloudService, IDashboardService dashboardService,
                                  com.example.seoulbike.dashboard.service.DashboardRealtimeMapService realtimeMapService) {
        this.wordCloudService = wordCloudService;
        this.realtimeMapService = realtimeMapService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/wordcloud")
    public List<DashboardWordCloudItem> getWordCloud(HttpSession session) {
        AuthResponse loginUser = (AuthResponse) session.getAttribute("loginUser");
        String region = (loginUser != null) ? loginUser.getRegion() : null;
        return wordCloudService.getWordCloudData(region);
    }

    @GetMapping("/realtime-map")
    public com.example.seoulbike.dashboard.model.DashboardRealtimeMapResponse getRealtimeMap(HttpSession session) {
        AuthResponse loginUser = (AuthResponse) session.getAttribute("loginUser");
        String region = (loginUser != null) ? loginUser.getRegion() : null;
        if (region == null) {
            // 기본값 처리 또는 에러 처리 (여기서는 빈 응답)
            return new com.example.seoulbike.dashboard.model.DashboardRealtimeMapResponse();
        }
        return realtimeMapService.getRealtimeMapData(region);
    }

    @GetMapping("/trend")
    public List<DashboardTrendPoint> getTrend(@RequestParam("periodType") String periodType) {
        return dashboardService.getUsageTrend(periodType);
    }
}
