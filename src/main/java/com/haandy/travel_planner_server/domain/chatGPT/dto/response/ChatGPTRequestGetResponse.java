package com.haandy.travel_planner_server.domain.chatGPT.dto.response;

import com.haandy.travel_planner_server.domain.chatGPT.data.ChatGPTRequest;
import lombok.Builder;

@Builder
public record ChatGPTRequestGetResponse(
        Number requestId,
        Number answerCode, // category
        String responseCity,
        String responseStartDt,
        String responseEndDt,
        String responseContent
) {
    public static ChatGPTRequestGetResponse from(ChatGPTRequest chatGPTRequest) {
        return ChatGPTRequestGetResponse.builder()
                .requestId(chatGPTRequest.getResponseId())
                .answerCode(chatGPTRequest.getAnswerCode())
                .responseCity(chatGPTRequest.getResponseCity())
                .responseStartDt(chatGPTRequest.getResponseStartDt())
                .responseEndDt(chatGPTRequest.getResponseEndDt())
                .responseContent(chatGPTRequest.getResponseContent())
                .build();
    }
}
