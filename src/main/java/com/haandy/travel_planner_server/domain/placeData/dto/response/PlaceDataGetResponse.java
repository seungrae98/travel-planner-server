package com.haandy.travel_planner_server.domain.placeData.dto.response;

import com.haandy.travel_planner_server.domain.placeData.data.PlaceData;
import lombok.Builder;

@Builder
public record PlaceDataGetResponse(
        String name,
        String post_address,
        String[] open_time,
        String price,
        String url_link
) {
    public static PlaceDataGetResponse from(PlaceData placeData) {
        return PlaceDataGetResponse.builder()
                .name(placeData.getName())
                .post_address(placeData.getPost_address())
                .open_time(placeData.getOpen_time())
                .price(placeData.getPrice())
                .url_link(placeData.getUrl_link())
                .build();
    }
}