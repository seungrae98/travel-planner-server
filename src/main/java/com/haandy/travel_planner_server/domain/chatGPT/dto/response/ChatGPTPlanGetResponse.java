package com.haandy.travel_planner_server.domain.chatGPT.dto.response;

import com.haandy.travel_planner_server.domain.chatGPT.data.ChatGPTPlan;
import lombok.Builder;

@Builder
public record ChatGPTPlanGetResponse(
        String day,
        String travel_destination_1,
        String travel_destination_2,
        String travel_destination_3,
        String restaurant_1,
        String restaurant_2,
        String restaurant_3
) {
    public static ChatGPTPlanGetResponse from(ChatGPTPlan chatGPTPlan) {
        return ChatGPTPlanGetResponse.builder()
                .day(chatGPTPlan.getDay())
                .travel_destination_1(chatGPTPlan.getTravel_destination_1())
                .travel_destination_2(chatGPTPlan.getTravel_destination_2())
                .travel_destination_3(chatGPTPlan.getTravel_destination_3())
                .restaurant_1(chatGPTPlan.getRestaurant_1())
                .restaurant_2(chatGPTPlan.getRestaurant_2())
                .restaurant_3(chatGPTPlan.getRestaurant_3())
                .build();
    }
}
