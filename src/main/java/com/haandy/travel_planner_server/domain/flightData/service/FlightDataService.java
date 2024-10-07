package com.haandy.travel_planner_server.domain.flightData.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haandy.travel_planner_server.domain.flightData.dto.response.FlightDataGetResponse;
import com.haandy.travel_planner_server.domain.flightData.data.FlightDataWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightDataService {

    final ObjectMapper objectMapper;

    public List<FlightDataGetResponse> getFlightDataList(
            String dpt_apt,
            String arv_apt,
            String start_date,
            String end_date,
            String flightTyp
    ) {

        /**
         * TODO: 크롤링 파이썬 코드 실행
         *
         * dpt_apt:     출발 공항
         * arv_apt:     도착 공항
         * start_date:  출발 날짜
         * end_date:    도착 날짜
         */

        // 그 이후 json 파일 읽기
        String flight_type;
        if (flightTyp.equals("low_price"))
            flight_type = "low_price_flights.json";
        else
            flight_type = "popular_flights.json";
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(flight_type);
            FlightDataWrapper flightDataWrapper = objectMapper.readValue(inputStream, FlightDataWrapper.class);

            return flightDataWrapper.getPopular_flights().stream()
                    .map(FlightDataGetResponse::from)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
