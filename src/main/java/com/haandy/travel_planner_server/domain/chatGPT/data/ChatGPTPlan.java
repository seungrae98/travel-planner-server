package com.haandy.travel_planner_server.domain.chatGPT.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatGPTPlan {
    private String city; // 고정

    private String day; // n일차

    private String travel_destination_1;
    private String travel_destination_1_detail;

    private String travel_destination_2;
    private String travel_destination_2_detail;

    private String travel_destination_3;
    private String travel_destination_3_detail;

    private String restaurant_1;
    private String restaurant_1_detail;

    private String restaurant_2;
    private String restaurant_2_detail;

    private String restaurant_3;
    private String restaurant_3_detail;
}
