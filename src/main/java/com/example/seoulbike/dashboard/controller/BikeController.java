package com.example.seoulbike.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.context.MessageSource;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.seoulbike.service.BikeService;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@RestController
public class BikeController {

    @Autowired
    private BikeService bikeService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/api/bikes/download/csv")
    public ResponseEntity<byte[]> downloadBikeStatusCsv(Locale locale) {
        String csvContent = bikeService.downloadBikeStatusCsv();
        byte[] csvBytes = csvContent.getBytes(StandardCharsets.UTF_8);

        String dateStr = java.time.LocalDate.now().toString().replace("-", "");
        String filename = "seoul_bike_status_" + dateStr + ".csv";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("text/csv; charset=UTF-8"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        headers.setContentLength(csvBytes.length);

        // 다운로드 완료 알림 전송 (다국어 적용)
        String notificationMsg = messageSource.getMessage("notification.download.complete", null, locale);
        messagingTemplate.convertAndSend("/topic/notifications", notificationMsg);

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/api/bikes/realtime")
    public ResponseEntity<String> getRealTimeBikeStatus() {
        String jsonResult = bikeService.getRealTimeBikeStatus();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonResult);
    }
}
