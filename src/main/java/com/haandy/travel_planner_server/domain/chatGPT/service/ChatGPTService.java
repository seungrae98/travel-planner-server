package com.haandy.travel_planner_server.domain.chatGPT.service;

import com.haandy.travel_planner_server.domain.chatGPT.dto.request.ChatGPTRequestGetRequest;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTPlanGetResponse;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTRequestGetResponse;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTResponseGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatGPTService {

    public ChatGPTRequestGetResponse getChatGPTRequest(ChatGPTRequestGetRequest request) {
        /**
         * TODO: GPT에 요청 보내서 요청 타입 분류하기
         *
         * request.requestId:       요청 번호
         * request.requestContent:  사용자 요청
         */

        return new ChatGPTRequestGetResponse(1, 1, "Response from chatGPT");
    }

    public ChatGPTResponseGetResponse getChatGPTResponse(String requestId) {

         /**
         * TODO: GPT 응답 가져오기
         *
         * requestId: 요청 번호
         */

        return new ChatGPTResponseGetResponse("Response from chatGPT");
    }

    public List<ChatGPTPlanGetResponse> getChatGPTPlanList(String requestId) {

        /**
         * TODO: GPT 여행 계획 가져오기
         *
         * requestId: 요청 번호
         */

        return getDummyChatGPTPlanList();
    }

    // 여행 계획 더미 데이터
    public List<ChatGPTPlanGetResponse> getDummyChatGPTPlanList() {
        List<ChatGPTPlanGetResponse> chatGPTPlanList = new ArrayList<>();
        chatGPTPlanList.add(
                ChatGPTPlanGetResponse.builder()
                        .day("Day 1")
                        .travel_destination_1("Eiffel Tower")
                        .travel_destination_2("Louvre Museum")
                        .travel_destination_3("Notre Dame Cathedral")
                        .restaurant_1("Le Jules Verne")
                        .restaurant_2("Cafe de Flore")
                        .restaurant_3("Le Comptoir du Relais")
                        .build()
        );
        chatGPTPlanList.add(
                ChatGPTPlanGetResponse.builder()
                        .day("Day 2")
                        .travel_destination_1("Buckingham Palace")
                        .travel_destination_2("Tower of London")
                        .travel_destination_3("British Museum")
                        .restaurant_1("The Ledbury")
                        .restaurant_2("Dishoom")
                        .restaurant_3("Sketch")
                        .build()
        );
        chatGPTPlanList.add(
                ChatGPTPlanGetResponse.builder()
                        .day("Day 3")
                        .travel_destination_1("Statue of Liberty")
                        .travel_destination_2("Central Park")
                        .travel_destination_3("Times Square")
                        .restaurant_1("Eleven Madison Park")
                        .restaurant_2("Joe's Pizza")
                        .restaurant_3("Katz's Delicatessen")
                        .build()
        );
        return chatGPTPlanList;
    }
}
