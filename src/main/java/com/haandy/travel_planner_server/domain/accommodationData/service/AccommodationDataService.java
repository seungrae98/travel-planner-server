package com.haandy.travel_planner_server.domain.accommodationData.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haandy.travel_planner_server.domain.accommodationData.dto.response.AccommodationDataGetResponse;
import com.haandy.travel_planner_server.domain.accommodationData.data.AccommodationDataWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccommodationDataService {

    final ObjectMapper objectMapper;

    public List<AccommodationDataGetResponse> getAccommodationDataList(
            String location,
            String start_date,
            String end_date,
            String accommodationType
    ) {

        /**
         * TODO: 크롤링 파이썬 코드 실행
         *
         * location:    숙소 장소
         * start_date:  출발 날짜
         * end_date:    도착 날짜
         */

        // 그 이후 json 파일 읽기
        String accommodation_type;
        if (accommodationType.equals("low_price"))
            accommodation_type = "low_price_accommodation.json";
        else
            accommodation_type = "accommodation.json";
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(accommodation_type);
            AccommodationDataWrapper flightDataWrapper = objectMapper.readValue(inputStream, AccommodationDataWrapper.class);

            return flightDataWrapper.getAccommodationDataList().stream()
                    .map(AccommodationDataGetResponse::from)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
