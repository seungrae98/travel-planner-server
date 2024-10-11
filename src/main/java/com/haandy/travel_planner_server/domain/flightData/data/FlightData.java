package com.haandy.travel_planner_server.domain.flightData.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightData {
    private String flightCode;
    private String airline;
    private String airline_logo;
    private String dpt_dpt_time;
    private String dpt_dpt_apt;
    private String dpt_arv_time;
    private String dpt_arv_apt;
    private String dpt_time;
    private String rtn_dpt_time;
    private String rtn_dpt_apt;
    private String rtn_arv_time;
    private String rtn_arv_apt;
    private String rtn_time;
    private String price;
    private String url;
}