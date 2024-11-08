package com.haandy.travel_planner_server.domain.directionData.controller;


import com.haandy.travel_planner_server.domain.directionData.dto.response.DirectionDataGetResponse;
import com.haandy.travel_planner_server.domain.directionData.service.DirectionDataService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/direction")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class DirectionDataController {

    private final DirectionDataService directionDataService;

    @GetMapping("/data")
    @Operation(summary = "경로 정보 조회하기")
    public ResponseEntity<DirectionDataGetResponse> getDirectionData(
            @RequestParam(value = "origin", defaultValue = "후쿠오카 공항") String origin,
            @RequestParam(value = "destination", defaultValue = "이치란 본점") String destination
    ) {
        System.out.println("origin: " + origin);
        System.out.println("destination: " + destination);
        return ResponseEntity.ok(directionDataService.getDirectionData(origin, destination));
    }
}
