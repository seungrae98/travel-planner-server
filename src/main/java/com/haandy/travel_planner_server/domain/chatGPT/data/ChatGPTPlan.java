package com.haandy.travel_planner_server.domain.chatGPT.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatGPTPlan {
    private String day;
    private String travel_destination_1;
    private String travel_destination_2;
    private String travel_destination_3;
    private String restaurant_1;
    private String restaurant_2;
    private String restaurant_3;
}
