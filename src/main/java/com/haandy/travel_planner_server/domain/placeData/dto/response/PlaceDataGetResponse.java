package com.haandy.travel_planner_server.domain.placeData.dto.response;

import com.haandy.travel_planner_server.domain.placeData.data.PlaceData;
import lombok.Builder;

@Builder
public record PlaceDataGetResponse(
        String name,
        String[] photo,
        String address,
        String[] open_hours,
        String admission_fee,
        String admission_url
) {
    public static PlaceDataGetResponse from(PlaceData placeData) {
        return PlaceDataGetResponse.builder()
                .name(placeData.getName())
                .photo(placeData.getPhoto())
                .address(placeData.getAddress())
                .open_hours(placeData.getOpen_hours())
                .admission_fee(placeData.getAdmission_fee())
                .admission_url(placeData.getAdmission_url())
                .build();
    }
}