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
    @Operation(summary = "항공권 정보 조회하기")
    public ResponseEntity<List<PlaceDataGetResponse>> getPlaceData(
            @RequestParam(value = "place", defaultValue = "서울") String place

    ) {
        System.out.println("place: " + place);
        return ResponseEntity.ok(placeDataService.getPlaceDataList(place));
    }
}
