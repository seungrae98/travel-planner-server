package com.haandy.travel_planner_server.domain.flightData.dto.response;

import com.haandy.travel_planner_server.domain.flightData.data.FlightData;
import lombok.Builder;

@Builder
public record FlightDataGetResponse (
        String airline,
        String airline_logo,
        String dpt_dpt_time,
        String dpt_dpt_apt,
        String dpt_arv_time,
        String dpt_arv_apt,
        String dpt_time,
        String rtn_dpt_time,
        String rtn_dpt_apt,
        String rtn_arv_time,
        String rtn_arv_apt,
        String rtn_time,
        String price,
        String url
) {
    public static FlightDataGetResponse from(FlightData flightData) {
        return FlightDataGetResponse.builder()
                .airline(flightData.getAirline())
                .airline_logo(flightData.getAirline_logo())
                .dpt_dpt_time(flightData.getDpt_dpt_time())
                .dpt_dpt_apt(flightData.getDpt_dpt_apt())
                .dpt_arv_time(flightData.getDpt_arv_time())
                .dpt_arv_apt(flightData.getDpt_arv_apt())
                .dpt_time(flightData.getDpt_time())
                .rtn_dpt_time(flightData.getRtn_dpt_time())
                .rtn_dpt_apt(flightData.getRtn_dpt_apt())
                .rtn_arv_time(flightData.getRtn_arv_time())
                .rtn_arv_apt(flightData.getRtn_arv_apt())
                .rtn_time(flightData.getRtn_time())
                .price(flightData.getPrice())
                .url(flightData.getUrl())
                .build();
    }
}