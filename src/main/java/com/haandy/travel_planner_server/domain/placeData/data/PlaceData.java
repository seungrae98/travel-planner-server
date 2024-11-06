package com.haandy.travel_planner_server.domain.placeData.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceData {
    private String name;
    private String[] photo;
    private String post_address;
    private String[] open_time;
    private String price;
    private String url_link;
}