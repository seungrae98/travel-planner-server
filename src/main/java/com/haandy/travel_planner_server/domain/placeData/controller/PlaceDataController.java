package com.haandy.travel_planner_server.domain.placeData.controller;


import com.haandy.travel_planner_server.domain.placeData.dto.response.PlaceDataGetResponse;
import com.haandy.travel_planner_server.domain.placeData.service.PlaceDataService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/place")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class PlaceDataController {

    private final PlaceDataService placeDataService;

    @GetMapping("/data")
    @Operation(summary = "여행지 상세 정보 조회하기")
    public ResponseEntity<PlaceDataGetResponse> getPlaceData(
            @RequestParam(value = "place", defaultValue = "에펠탑 프랑스 파리") String place

    ) {
        System.out.println("place: " + place);
        return ResponseEntity.ok(placeDataService.getPlaceData(place));
    }
}
