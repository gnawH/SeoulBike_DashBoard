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
import java.util.List;
import java.util.Locale;

@RestController
public class BikeController {

    @Autowired
    private BikeService bikeService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private com.example.seoulbike.dashboard.service.DashboardRealtimeMapService realtimeMapService;

    @GetMapping("/api/bikes/download/csv")
    public ResponseEntity<byte[]> downloadBikeStatusCsv(jakarta.servlet.http.HttpSession session, Locale locale) {
        com.example.seoulbike.auth.model.AuthResponse loginUser = (com.example.seoulbike.auth.model.AuthResponse) session.getAttribute("loginUser");
        String region = (loginUser != null) ? loginUser.getRegion() : "전체";
        
        List<com.example.seoulbike.dashboard.model.DashboardStationOccupancy> allStations = realtimeMapService.getAllStationsOccupancy(region);
        
        byte[] excelBytes = buildCategorizedExcel(allStations);
        
        String dateStr = java.time.LocalDate.now().toString().replace("-", "");
        String filename = "seoul_bike_status_" + dateStr + ".xlsx";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        headers.setContentLength(excelBytes.length);

        // 다운로드 완료 알림 전송 (다국어 적용)
        String notificationMsg = messageSource.getMessage("notification.download.complete", null, locale);
        messagingTemplate.convertAndSend("/topic/notifications", notificationMsg);

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/api/bikes/realtime")
    public ResponseEntity<String> getRealTimeBikeStatus() {
        String jsonResult = bikeService.getRealTimeBikeStatus();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonResult);
    }

    private byte[] buildCategorizedExcel(List<com.example.seoulbike.dashboard.model.DashboardStationOccupancy> stations) {
        try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            
            // 첫 번째 시트: 100% 이하 (오름차순) -> '안정_보충_대여소'
            org.apache.poi.ss.usermodel.Sheet sheet1 = workbook.createSheet("안정_보충_대여소");
            org.apache.poi.ss.usermodel.Row header1 = sheet1.createRow(0);
            header1.createCell(0).setCellValue("대여소 ID");
            header1.createCell(1).setCellValue("대여소명");
            header1.createCell(2).setCellValue("지역구");
            header1.createCell(3).setCellValue("전체 거치대 수");
            header1.createCell(4).setCellValue("현재 주차된 자전거");
            header1.createCell(5).setCellValue("거치율 (%)");

            java.util.concurrent.atomic.AtomicInteger rowIdx1 = new java.util.concurrent.atomic.AtomicInteger(1);
            stations.stream()
                    .filter(s -> s.getOccupancyRate() <= 100.0)
                    .sorted(java.util.Comparator.comparingDouble(com.example.seoulbike.dashboard.model.DashboardStationOccupancy::getOccupancyRate)
                            .thenComparing(com.example.seoulbike.dashboard.model.DashboardStationOccupancy::getStationId))
                    .forEach(s -> {
                        org.apache.poi.ss.usermodel.Row row = sheet1.createRow(rowIdx1.getAndIncrement());
                        row.createCell(0).setCellValue(s.getStationId());
                        row.createCell(1).setCellValue(s.getStationName());
                        row.createCell(2).setCellValue(s.getRegionName());
                        row.createCell(3).setCellValue(s.getRackTotCnt());
                        row.createCell(4).setCellValue(s.getParkingBikeTotCnt());
                        row.createCell(5).setCellValue(s.getOccupancyRate());
                    });

            // 두 번째 시트: 100% 초과 (내림차순) -> '회수_필요_대여소'
            org.apache.poi.ss.usermodel.Sheet sheet2 = workbook.createSheet("회수_필요_대여소");
            org.apache.poi.ss.usermodel.Row header2 = sheet2.createRow(0);
            header2.createCell(0).setCellValue("대여소 ID");
            header2.createCell(1).setCellValue("대여소명");
            header2.createCell(2).setCellValue("지역구");
            header2.createCell(3).setCellValue("전체 거치대 수");
            header2.createCell(4).setCellValue("현재 주차된 자전거");
            header2.createCell(5).setCellValue("거치율 (%)");
            
            java.util.concurrent.atomic.AtomicInteger rowIdx2 = new java.util.concurrent.atomic.AtomicInteger(1);
            stations.stream()
                    .filter(s -> s.getOccupancyRate() > 100.0)
                    .sorted(java.util.Comparator.comparingDouble(com.example.seoulbike.dashboard.model.DashboardStationOccupancy::getOccupancyRate).reversed()
                            .thenComparing(com.example.seoulbike.dashboard.model.DashboardStationOccupancy::getStationId))
                    .forEach(s -> {
                        org.apache.poi.ss.usermodel.Row row = sheet2.createRow(rowIdx2.getAndIncrement());
                        row.createCell(0).setCellValue(s.getStationId());
                        row.createCell(1).setCellValue(s.getStationName());
                        row.createCell(2).setCellValue(s.getRegionName());
                        row.createCell(3).setCellValue(s.getRackTotCnt());
                        row.createCell(4).setCellValue(s.getParkingBikeTotCnt());
                        row.createCell(5).setCellValue(s.getOccupancyRate());
                    });

            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            workbook.write(bos);
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
