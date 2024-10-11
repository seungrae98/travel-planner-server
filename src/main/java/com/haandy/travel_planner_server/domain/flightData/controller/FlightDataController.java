package com.haandy.travel_planner_server.domain.flightData.controller;


import com.haandy.travel_planner_server.domain.flightData.dto.response.FlightDataGetResponse;
import com.haandy.travel_planner_server.domain.flightData.service.FlightDataService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flight")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class FlightDataController {

    private final FlightDataService flightDataService;

    @GetMapping("/data")
    @Operation(summary = "항공권 정보 조회하기")
    public ResponseEntity<List<FlightDataGetResponse>> getFlightData(
            @RequestParam(value = "origin", defaultValue = "서울") String origin,
            @RequestParam(value = "destination", defaultValue = "로스엔젤레스") String destination,
            @RequestParam(value = "start_date", defaultValue = "20250301") String start_date,
            @RequestParam(value = "end_date", defaultValue = "20250310") String end_date
    ) {
        System.out.println("origin: " + origin);
        System.out.println("destination: " + destination);
        System.out.println("start_date: " + start_date);
        System.out.println("end_date: " + end_date);
        return ResponseEntity.ok(flightDataService.getFlightDataList(origin, destination, start_date, end_date));
    }
}
