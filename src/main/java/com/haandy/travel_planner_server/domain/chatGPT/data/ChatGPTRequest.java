package com.haandy.travel_planner_server.domain.chatGPT.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatGPTRequest {
    private Number responseId;
    private Number answerCode;
    private String responseCity;
    private String responseStartDt;
    private String responseEndDt;
    private String responseContent;
}
