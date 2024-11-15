package com.haandy.travel_planner_server.domain.placeData.dto.response;

import com.haandy.travel_planner_server.domain.placeData.data.PlaceData;
import lombok.Builder;

@Builder
public record PlaceDataGetResponse(
        String name,
        String[] photo,
        String address,
        String latitude,
        String longitude,
        String[] opening_hours,
        String admission_provider,
        String admission_fee,
        String admission_url,
        String web_site
) {
    public static PlaceDataGetResponse from(PlaceData placeData) {
        return PlaceDataGetResponse.builder()
                .name(placeData.getName())
                .photo(placeData.getPhoto())
                .address(placeData.getAddress())
                .latitude(placeData.getLatitude())
                .longitude(placeData.getLongitude())
                .opening_hours(placeData.getOpening_hours())
                .admission_provider(placeData.getAdmission_provider())
                .admission_fee(placeData.getAdmission_fee())
                .admission_url(placeData.getAdmission_url())
                .web_site(placeData.getWeb_site())
                .build();
    }
}