package com.example.seoulbike.dashboard.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeoulBikeApiResult {

    @JsonProperty("rentBikeStatus")
    private RentBikeStatus rentBikeStatus;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RentBikeStatus {
        @JsonProperty("list_total_count")
        private int listTotalCount;

        @JsonProperty("row")
        private List<SeoulBikeApiRow> rows;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SeoulBikeApiRow {
        @JsonProperty("stationId")
        private String stationId;

        @JsonProperty("stationName")
        private String stationName;

        @JsonProperty("shared")
        private String shared; // 거치율(%) (보통 숫자로 주지만 문자열로 올 수 있음)
        
        @JsonProperty("stationLatitude")
        private String stationLatitude;
        
        @JsonProperty("stationLongitude")
        private String stationLongitude;

        @JsonProperty("rackTotCnt")
        private String rackTotCnt;

        @JsonProperty("parkingBikeTotCnt")
        private String parkingBikeTotCnt;
    }
}
