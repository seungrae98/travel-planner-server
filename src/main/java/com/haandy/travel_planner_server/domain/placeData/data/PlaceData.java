package com.haandy.travel_planner_server.domain.placeData.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceData {
    private String name;
    private String[] photo;
    private String address;
    private String[] open_hours;
    private String admission_fee;
    private String admission_url;
}