package com.haandy.travel_planner_server.domain.chatGPT.dto.response;

import com.haandy.travel_planner_server.domain.chatGPT.data.ChatGPTRequest;
import lombok.Builder;

@Builder
public record ChatGPTRequestGetResponse(
        Number requestId,
        Number answerCode,
        String responseContent
) {
    public static ChatGPTRequestGetResponse from(ChatGPTRequest chatGPTRequest) {
        return ChatGPTRequestGetResponse.builder()
                .requestId(chatGPTRequest.getResponseId())
                .answerCode(chatGPTRequest.getAnswerCode())
                .responseContent(chatGPTRequest.getResponseContent())
                .build();
    }
}
