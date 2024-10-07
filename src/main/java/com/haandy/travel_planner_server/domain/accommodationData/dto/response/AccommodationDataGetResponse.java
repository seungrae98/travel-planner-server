package com.haandy.travel_planner_server.domain.accommodationData.dto.response;

import com.haandy.travel_planner_server.domain.accommodationData.data.AccommodationData;
import lombok.Builder;

@Builder
public record AccommodationDataGetResponse(
        String photo,
        String name,
        String location,
        String rate,
        String price,
        String url
) {
    public static AccommodationDataGetResponse from(AccommodationData accommodationData) {
        return AccommodationDataGetResponse.builder()
                .photo(accommodationData.getPhoto())
                .name(accommodationData.getName())
                .location(accommodationData.getLocation())
                .rate(accommodationData.getRate())
                .price(accommodationData.getPrice())
                .url(accommodationData.getUrl())
                .build();
    }
}