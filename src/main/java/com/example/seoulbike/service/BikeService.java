package com.example.seoulbike.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BikeService {

    @Value("${seoul.api.key}")
    private String apiKey;

    private static final String API_BASE_URL = "http://openapi.seoul.go.kr:8088/";

    public String downloadBikeStatusCsv() {
        // ... (existing logic)
        StringBuilder csv = new StringBuilder();
        csv.append("\uFEFF");
        csv.append("Station ID,Station Name,Total Racks,Parking Bikes,Occupancy Rate (%)\n");

        try {
            String response = fetchFromApi();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode rows = root.path("rentBikeStatus").path("row");

            if (rows.isArray()) {
                for (JsonNode row : rows) {
                    csv.append(String.format("%s,%s,%s,%s,%s\n", 
                        row.path("stationId").asText().replace(",", "."),
                        row.path("stationName").asText().replace(",", "."),
                        row.path("rackTotCnt").asText(),
                        row.path("parkingBikeTotCnt").asText(),
                        row.path("shared").asText()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "\uFEFFError fetching data from Seoul Open API";
        }
        return csv.toString();
    }

    public String getRealTimeBikeStatus() {
        return fetchFromApi();
    }

    private String getFullUrl(int start, int end) {
        return API_BASE_URL + apiKey + "/json/bikeList/" + start + "/" + end + "/";
    }

    private String fetchFromApi() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.getForObject(getFullUrl(1, 1000), String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
}
