package com.haandy.travel_planner_server.domain.chatGPT.service;

import com.haandy.travel_planner_server.domain.chatGPT.data.ChatGPTPlan;
import com.haandy.travel_planner_server.domain.chatGPT.dto.request.ChatGPTRequestGetRequest;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTPlanGetResponse;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTRequestGetResponse;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTResponseGetResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ChatGPTService {

    private int DAYS = 1;
    private String RESPONSE; // ChatGPT 생 응답
    private JSONObject JSONOBJECT; // Clean json

    public String processChatGPT(String message) {
        try {
            // ChatGPTService에서 처리된 응답을 문자열로 받아옴
            System.out.println("User Message: \n" + message);

            String prefix = "너는 내 여행 계획 비서이며, 나에게 여행 계획을 짜줘야해. 나는 네 여행 계획을 참고해서 여행을 할거야.\n";
            String suffix = """         
                    \n
                      사용자의 문장을 분석해서 JSON 파일 형식으로 출력해.
                      
                      모든 출력 필수 키값은\s
                      { “category”, “response_message”, “content” }\s
                      이며 아래 요구사항에 맞게 출력해
                      
                      category: 사용자 문장의 목적이 아래 카테고리 중 어디에 해당하는지 분석하여 번호 만을 출력해
                      1. 여행 계획 추천
                      2. 여행 일정 변경
                      3. 여행지 정보 검색
                      4. 일정 간 경로 검색
                      5. 해당 없음
                      
                      response_message: 사용자 문장에 대한 챗지피티 답변. 그것이 만약\s
                      1번 카테고리일 경우: 여행도시와 여행날짜 정보가 명확하지 않다면 다시 물어보고, 명확하다면 사용자 문장에 맞는 챗지피티 답변을 출력해.
                      5번 카테고리일 경우: 여행 계획과 관련된 질문을 하라고 출력해.\s
                      
                      content: 카테고리마다 아래와 같이 다른 키값을 하위에 출력해.
                      1번 카테고리일 경우:\s
                      {\s
                      	travel_city,\s
                      	travel_start_date: MM/dd,\s
                      	travel_end_date: MM/dd,\s
                      	travel_days: dd (총 여행 일 수),
                      	attraction_list: 추천 관광지,
                      	restaurant_list: 추천 식당
                      }
                      단, attraction_list, restaurant_list는 아래에 키값을 배열로 나열해
                      	{
                      		day: 여행 몇 일차(int),\s
                      		name: 장소 이름, \s
                      		detail: 추천이유 혹은 장소 설명\s
                      	}
                      	(attraction과 restaurant는 반드시 각 날짜별로 하루에 3개씩 추천하고, detail에는 ,를 포함시키지 마.)
                      
                      2, 3, 4, 5번 카테고리일 경우 값은 0이야.
                    """;

            message = prefix + '\n' + message + suffix;
            RESPONSE = getChatGPTResponse(message);
            System.out.println("ChatGPT Response: \n" + RESPONSE + "\n===================================");

            return RESPONSE;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }

    // ChatGPT와의 통신을 통해 메시지 결과를 받아옴
    public String getChatGPTResponse(String message) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("python3", "python/chatgpt.py", message);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        // 응답을 문자열 형태로 반환
        return result.toString();
    }

    // 받아온 chatGPT 메시지 결과를 카테고리에 맞게 추출 함수를 호출
    public int categorizeChatGPT(String response) {

        // JSONObject 생성
        String cleanedJson = response.replaceAll("^```json|```$", "").trim();
        JSONOBJECT = new JSONObject(cleanedJson);

        // category 객체 가져오기
        int category = JSONOBJECT.getInt("category");
        System.out.println("Category: " + category);

        return category;
    }

    public ChatGPTRequestGetResponse getChatGPTRequest(ChatGPTRequestGetRequest request) { // 몇 번 category?
        /*
         * TODO: GPT에 요청 보내서 요청 타입 분류하기 (완)
         *
         * request.requestId:       요청 번호
         * request.requestContent:  사용자 요청
         */
        int category = 0;
        try {
            processChatGPT(request.requestContent());
            category = categorizeChatGPT(RESPONSE);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred: " + e.getMessage());
        }

        return new ChatGPTRequestGetResponse(1, category, "Response from chatGPT");
        // return new ChatGPTRequestGetResponse(request.requestId(), category, "Response from chatGPT");
    }

    public ChatGPTResponseGetResponse getChatGPTResponseId(String requestId) {

        /**
         * TODO: GPT 응답(계획 외 정보) 가져오기
         *
         * requestId: 요청 번호
         */

        return new ChatGPTResponseGetResponse("Response from chatGPT");
    }

    public List<ChatGPTPlanGetResponse> getChatGPTPlanList(String requestId) {

        /**
         * TODO: GPT 여행 계획 가져오기 (완)
         *
         * requestId: 요청 번호
         */
        return getDummyChatGPTPlanList();
    }

    // 여행 계획 더미 데이터
    public List<ChatGPTPlanGetResponse> getDummyChatGPTPlanList() {
        List<ChatGPTPlanGetResponse> chatGPTPlanList = new ArrayList<>();

        // content 객체 가져오기
        JSONObject content = JSONOBJECT.getJSONObject("content");

        String city = content.getString("travel_city");
        DAYS = content.getInt("travel_days");

        // attraction_list에서 각 요소의 name과 detail 값을 배열로 받기
        JSONArray attractionList = content.getJSONArray("attraction_list");
        List<String> attractionNames = new ArrayList<>();
        List<String> attractionDetails = new ArrayList<>();
        for (int i = 0; i < attractionList.length(); i++) {
            JSONObject attraction = attractionList.getJSONObject(i);
            attractionNames.add(attraction.getString("name"));
            attractionDetails.add(attraction.getString("detail"));
        }
        System.out.println("Attractions: " + attractionNames);
        System.out.println("Attraction Details: " + attractionDetails);

        // restaurant_list에서 각 요소의 name과 detail 값을 배열로 받기
        JSONArray restaurantList = content.getJSONArray("restaurant_list");
        List<String> restaurantNames = new ArrayList<>();
        List<String> restaurantDetails = new ArrayList<>();
        for (int i = 0; i < restaurantList.length(); i++) {
            JSONObject restaurant = restaurantList.getJSONObject(i);
            restaurantNames.add(restaurant.getString("name"));
            restaurantDetails.add(restaurant.getString("detail"));
        }
        System.out.println("Restaurants: " + restaurantNames);
        System.out.println("Restaurant Details: " + restaurantDetails);

        ChatGPTPlan chatGPTPlan = new ChatGPTPlan();

        Iterator<String> attrNmIt = attractionNames.iterator();
        Iterator<String> attrDtIt = attractionDetails.iterator();
        Iterator<String> restNmIt = restaurantNames.iterator();
        Iterator<String> restDtIt = restaurantDetails.iterator();

        for (int i = 0; i < DAYS; i++) {
            chatGPTPlanList.add(
                    ChatGPTPlanGetResponse.builder()
                            .city(city)
                            .day("Day" + (i + 1))
                            .travel_destination_1(attrNmIt.next())
                            .travel_destination_1_detail(attrDtIt.next())
                            .travel_destination_2(attrNmIt.next())
                            .travel_destination_2_detail(attrDtIt.next())
                            .travel_destination_3(attrNmIt.next())
                            .travel_destination_3_detail(attrDtIt.next())
                            .restaurant_1(restNmIt.next())
                            .restaurant_1_detail(restDtIt.next())
                            .restaurant_2(restNmIt.next())
                            .restaurant_2_detail(restDtIt.next())
                            .restaurant_3(restNmIt.next())
                            .restaurant_3_detail(restDtIt.next())
                            .build()
            );
        }

        System.out.println("chatGPTPlanList : " + chatGPTPlanList);

        return chatGPTPlanList;
    }
}