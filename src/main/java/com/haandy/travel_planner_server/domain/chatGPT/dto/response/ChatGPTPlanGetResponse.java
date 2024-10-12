package com.haandy.travel_planner_server.domain.chatGPT.dto.response;

import com.haandy.travel_planner_server.domain.chatGPT.data.ChatGPTPlan;
import lombok.Builder;

@Builder
public record ChatGPTPlanGetResponse(
        String city,
        String day,
        String travel_destination_1,
        String travel_destination_1_time,
        String travel_destination_1_detail,
        String travel_destination_2,
        String travel_destination_2_time,
        String travel_destination_2_detail,
        String travel_destination_3,
        String travel_destination_3_time,
        String travel_destination_3_detail,
        String restaurant_1,
        String restaurant_1_time,
        String restaurant_1_detail,
        String restaurant_2,
        String restaurant_2_time,
        String restaurant_2_detail,
        String restaurant_3,
        String restaurant_3_time,
        String restaurant_3_detail
) {
    public static ChatGPTPlanGetResponse from(ChatGPTPlan chatGPTPlan) {
        return ChatGPTPlanGetResponse.builder()
                .city(chatGPTPlan.getCity())
                .day(chatGPTPlan.getDay())
                .travel_destination_1(chatGPTPlan.getTravel_destination_1())
                .travel_destination_1_time(chatGPTPlan.getTravel_destination_1_time())
                .travel_destination_1_detail(chatGPTPlan.getTravel_destination_1_detail())
                .travel_destination_2(chatGPTPlan.getTravel_destination_2())
                .travel_destination_2_time(chatGPTPlan.getTravel_destination_2_time())
                .travel_destination_2_detail(chatGPTPlan.getTravel_destination_2_detail())
                .travel_destination_3(chatGPTPlan.getTravel_destination_3())
                .travel_destination_3_time(chatGPTPlan.getTravel_destination_3_time())
                .travel_destination_3_detail(chatGPTPlan.getTravel_destination_3_detail())
                .restaurant_1(chatGPTPlan.getRestaurant_1())
                .restaurant_1_time(chatGPTPlan.getRestaurant_1_time())
                .restaurant_1_detail(chatGPTPlan.getRestaurant_1_detail())
                .restaurant_2(chatGPTPlan.getRestaurant_2())
                .restaurant_2_time(chatGPTPlan.getRestaurant_2_time())
                .restaurant_2_detail(chatGPTPlan.getRestaurant_2_detail())
                .restaurant_3(chatGPTPlan.getRestaurant_3())
                .restaurant_3_time(chatGPTPlan.getRestaurant_3_time())
                .restaurant_3_detail(chatGPTPlan.getRestaurant_3_detail())
                .build();
    }
}
