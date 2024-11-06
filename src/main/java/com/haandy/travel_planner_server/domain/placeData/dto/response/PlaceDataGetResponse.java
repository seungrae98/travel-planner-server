package com.haandy.travel_planner_server.domain.placeData.dto.response;

import com.haandy.travel_planner_server.domain.placeData.data.PlaceData;
import lombok.Builder;

@Builder
public record PlaceDataGetResponse(
        String name,
        String[] photo,
        String address,
        String[] opening_hours,
        String admission_fee,
        String web_site
) {
    public static PlaceDataGetResponse from(PlaceData placeData) {
        return PlaceDataGetResponse.builder()
                .name(placeData.getName())
                .photo(placeData.getPhoto())
                .address(placeData.getAddress())
                .opening_hours(placeData.getOpening_hours())
                .admission_fee(placeData.getAdmission_fee())
                .web_site(placeData.getWeb_site())
                .build();
    }
}