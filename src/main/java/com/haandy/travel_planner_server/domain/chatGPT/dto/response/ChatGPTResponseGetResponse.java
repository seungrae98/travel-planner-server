package com.haandy.travel_planner_server.domain.chatGPT.dto.response;

import com.haandy.travel_planner_server.domain.chatGPT.data.ChatGPTResponse;
import lombok.Builder;

@Builder
public record ChatGPTResponseGetResponse(
        String responseContent
) {
    public static ChatGPTResponseGetResponse from(ChatGPTResponse chatGPTResponse) {
        return ChatGPTResponseGetResponse.builder()
                .responseContent(chatGPTResponse.getResponse())
                .build();
    }
}
