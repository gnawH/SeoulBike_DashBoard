package com.example.seoulbike.dashboard.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.seoulbike.auth.model.AuthResponse;
import com.example.seoulbike.dashboard.model.DashboardWordCloudItem;
import com.example.seoulbike.dashboard.service.DashboardWordCloudService;

import jakarta.servlet.http.HttpSession;

/**
 * DashboardController
 *
 * 대시보드 페이지 라우팅 및 데이터 연동을 담당하는 컨트롤러입니다.
 * 역할을 모델 담당 업무 중 서비스 데이터를 뷰(Thymeleaf)로 넘기는 기초 연동을 수행합니다.
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);

    private final DashboardWordCloudService wordCloudService;

    public DashboardController(DashboardWordCloudService wordCloudService) {
        this.wordCloudService = wordCloudService;
    }

    /**
     * 대시보드 메인 페이지 노출 (사용자별 워드클라우드 포함)
     */
    @GetMapping
    public String dashboardPage(HttpSession session, Model model) {
        long totalStart = System.currentTimeMillis();

        // 1. 세션에서 로그인 사용자(담당구 정보 포함)를 가져옴
        AuthResponse loginUser = (AuthResponse) session.getAttribute("loginUser");

        if (loginUser == null) {
            log.warn("[DASHBOARD] 접근 실패 - 로그인 세션 없음");
            return "redirect:/login"; // 로그인 페이지로 리다이렉트 (가정)
        }

        log.info("[DASHBOARD] 데이터 생성 시작 - userId: {}, 담당구역: {}", loginUser.getUserId(), loginUser.getRegion());

        // 2. 워드클라우드 데이터 생성 (Service 호출)
        // 서비스 내부에서 단계별 로그와 시간을 측정함
        List<DashboardWordCloudItem> wordCloudData = wordCloudService.getWordCloudData(loginUser.getRegion());

        // 3. 뷰로 데이터 전달
        model.addAttribute("wordCloudData", wordCloudData);
        model.addAttribute("loginUser", loginUser);

        long totalElapsed = System.currentTimeMillis() - totalStart;
        log.info("[DASHBOARD] 모든 데이터 생성 완료 | 총 소요시간: {}ms", totalElapsed);

        return "dashboard/user"; // templates/dashboard/user.html 렌더링
    }
}
