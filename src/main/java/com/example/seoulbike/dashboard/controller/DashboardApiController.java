package com.example.seoulbike.dashboard.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.seoulbike.auth.model.AuthResponse;
import com.example.seoulbike.dashboard.model.DashboardWordCloudItem;
import com.example.seoulbike.dashboard.service.DashboardWordCloudService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApiController {

    private final DashboardWordCloudService wordCloudService;

    public DashboardApiController(DashboardWordCloudService wordCloudService) {
        this.wordCloudService = wordCloudService;
    }

    @GetMapping("/wordcloud")
    public List<DashboardWordCloudItem> getWordCloud(HttpSession session) {
        AuthResponse loginUser = (AuthResponse) session.getAttribute("loginUser");
        String region = (loginUser != null) ? loginUser.getRegion() : null;
        return wordCloudService.getWordCloudData(region);
    }
}
