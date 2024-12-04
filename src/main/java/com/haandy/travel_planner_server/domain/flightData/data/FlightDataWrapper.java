package com.haandy.travel_planner_server.domain.flightData.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FlightDataWrapper {
    // Getter & Setter
    private List<FlightData> flights;

}
