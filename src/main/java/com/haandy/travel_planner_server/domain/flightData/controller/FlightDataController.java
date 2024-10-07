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

    @GetMapping("/{flightType}/data")
    @Operation(summary = "항공권 정보 조회하기")
    public ResponseEntity<List<FlightDataGetResponse>> getFlightData(
            @PathVariable("flightType") String flightType,
            @RequestParam(value = "dpt_apt", defaultValue = "ICN") String dpt_apt,
            @RequestParam(value = "arv_apt", defaultValue = "") String arv_apt,
            @RequestParam(value = "start_date", defaultValue = "0000-00-00") String start_date,
            @RequestParam(value = "end_date", defaultValue = "0000-00-00") String end_date
    ) {
        System.out.println("flightType: " + flightType);
        System.out.println("dpt_apt: " + dpt_apt);
        System.out.println("arv_apt: " + arv_apt);
        System.out.println("start_date: " + start_date);
        System.out.println("end_date: " + end_date);
        return ResponseEntity.ok(flightDataService.getFlightDataList(dpt_apt, arv_apt, start_date, end_date, flightType));
    }
}
