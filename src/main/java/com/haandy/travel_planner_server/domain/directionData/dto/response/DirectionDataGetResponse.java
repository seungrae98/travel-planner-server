package com.haandy.travel_planner_server.domain.directionData.dto.response;

import com.haandy.travel_planner_server.domain.directionData.data.DirectionData;
import lombok.Builder;

@Builder
public record DirectionDataGetResponse (
        String direction,
        String time
) {
    public static DirectionDataGetResponse from(DirectionData directionData) {
        return DirectionDataGetResponse.builder()
                .direction(directionData.getDirection())
                .time(directionData.getTime())
                .build();
    }
}