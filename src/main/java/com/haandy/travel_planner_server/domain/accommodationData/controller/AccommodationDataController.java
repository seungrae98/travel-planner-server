package com.haandy.travel_planner_server.domain.accommodationData.controller;

import com.haandy.travel_planner_server.domain.accommodationData.dto.request.AccommodationDataGetRequest;
import com.haandy.travel_planner_server.domain.accommodationData.dto.response.AccommodationDataGetResponse;
import com.haandy.travel_planner_server.domain.accommodationData.service.AccommodationDataService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accommodation")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AccommodationDataController {

    private final AccommodationDataService accommodationDataService;

    @GetMapping("/{accommodationType}/data")
    @Operation(summary = "숙소 정보 조회하기")
    public ResponseEntity<List<AccommodationDataGetResponse>> getAccommodationData(
            @PathVariable("accommodationType") String accommodationType,
            @RequestParam(value = "location", defaultValue = "ICN") String location,
            @RequestParam(value = "start_date", defaultValue = "0000-00-00") String start_date,
            @RequestParam(value = "end_date", defaultValue = "0000-00-00") String end_date
    ) {
        return ResponseEntity.ok(accommodationDataService.getAccommodationDataList(location, start_date, end_date, accommodationType));
    }
}
