package com.haandy.travel_planner_server.domain.chatGPT.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChatGPTRequestGetRequest(
        @Schema(example = "1")
        String requestId, // int
        @Schema(example = "여행 계획 만들어줘")
        String requestContent
) {

}
