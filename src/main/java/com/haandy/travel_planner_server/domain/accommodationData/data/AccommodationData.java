package com.haandy.travel_planner_server.domain.accommodationData.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccommodationData {
    private String photo;
    private String name;
    private String location;
    private String rate;
    private String price;
    private String url;
}