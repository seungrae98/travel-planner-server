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
    private String latitude;
    private String longitude;

    @JsonProperty("opening_hours")
    private String[] opening_hours;
    private String admission_provider;
    private String admission_fee;
    private String admission_url;
    private String web_site;
}