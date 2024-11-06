package com.haandy.travel_planner_server.domain.placeData.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceData {
    private String name;

    @JsonProperty("photo")
    private String[] photo;
    private String address;

    @JsonProperty("open_hours")
    private String[] open_hours;
    private String admission_fee;
    private String web_site;
}