package com.haandy.travel_planner_server.domain.chatGPT.service;

import com.haandy.travel_planner_server.domain.chatGPT.dto.request.ChatGPTRequestGetRequest;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTPlanGetResponse;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTRequestGetResponse;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTResponseGetResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatGPTService {

    private int DAYS;
    private String RESPONSE; // ChatGPT 응답
    private JSONObject JSONOBJECT; // cleaned JSON
    private List<ChatGPTPlanGetResponse> chatGPTPlanListCpy;
    private int REQUESTID = 1;
    private List<String> placeNameList = new ArrayList<>();

    // 사용자 문장에 우리 서비스 탬플릿 붙여서 chatGPT에 입력
    public String processChatGPT(String message) {
        try {
            System.out.println("User Message: \n" + message);

            String prefix = "너는 여행 계획 비서이며, 사용자에게 여행 계획을 짜 줘야 해. 너의 여행 계획을 참고해서 여행을 할 거야. 참고로 올해는 2024년이야.\n";
            String suffix = """         
                    \n
                      위의 사용자의 문장을 분석해서 JSON 파일 형식으로 출력해.
                    
                      모든 출력 필수 키값은
                      { “category”, “response_message”, “content” }
                      이며 아래 요구사항에 맞게 출력해
                    
                      category: 사용자 문장의 목적이 아래 카테고리 중 어디에 해당하는지 분석하여 번호 만을 출력해.
                      1. 새로운 여행 계획 생성 (여행 도시와 여행 날짜가 필수 포함되어야 해당 카테고리로 분류)
                      2. 기존 여행 일정 변경
                      3. 여행지 정보 검색
                      4. 일정 간 경로 검색
                      5. 여행 관련이 아닌 질문
                    
                      response_message: 사용자 문장에 대한 챗지피티 답변. 그것이 만약
                      1번 카테고리일 경우: 여행도시와 여행날짜 정보가 명확하지 않다면 다시 물어보고, 명확하다면 사용자 문장에 맞는 챗지피티 답변을 출력해.
                      2번 카테고리일 경우 : 새로운 추천 여행지 장소는 현재 계획된 여행지 장소에 포함되지 않은 장소를 제시해야해.
                      3번 카테고리일 경우 : 여행지 정보에 관한 상세한 내용을 20자 이상 작성해줘.
                      5번 카테고리일 경우: 너의 역할을 설명하고, 여행과 관련된 질문을 해달라고 출력해.
                    
                      content: 카테고리에 따라 아래 형식에 맞춰 키값을 하위에 출력해.
                      1번 카테고리일 경우:
                      {
                      travel_city: 여행 도시,
                      travel_start_date: yyyy-MM-dd,
                      travel_end_date: yyyy-MM-dd,
                      travel_days: 총 여행 일 수(int),
                      attraction_list: 추천 관광지,
                      restaurant_list: 추천 식당
                      }
                      단, attraction_list, restaurant_list는 아래에 키값을 배열로 나열해
                      {
                      day: 여행 몇 일차(int),
                      name: 장소 이름,
                      time: 추천 방문 시간(HH:mm),
                      detail: 추천이유 혹은 장소 설명
                      }
                      (attraction과 restaurant는 반드시 각 일차별로 하루에 3개씩 추천해주고 식사 시간은 아침 식사, 점심 식사, 저녁 식사 시간대로 추천해)
                      
                      2번 카테고리일 경우:
                      {
                      previous_name: 변경을 희망하는 장소명
                      name: 장소 이름,
                      detail: 추천이유 혹은 장소 설명
                      }
                      
                      3, 4, 5번 카테고리일 경우 값은 0이야.
                    
                    """;

            if (!placeNameList.isEmpty()) {
                prefix += "현재까지 계획된 여행지 장소 목록은 " + String.join(", ", placeNameList) + " 야";
                System.out.println(String.join(", ", placeNameList));
            }

            message = prefix + '\n' + message + suffix;
            RESPONSE = getChatGPTResponse(message);
            System.out.println("ChatGPT Response: \n" + RESPONSE + "\n===================================");

            return RESPONSE;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }

    // ChatGPT와의 통신을 통해 입력을 넣으면 메시지 결과를 받아옴
    public String getChatGPTResponse(String message) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("python3", "python/chatgpt.py", message);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        process.waitFor();  // 파이썬 프로세스가 종료될 때까지 대기

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        // 응답을 문자열 형태로 반환
        return result.toString();
    }

    // 받아온 chatGPT 응답으로 카테고리 번호 리턴
    public int categorizeChatGPT(String response) {

        // JSONObject 생성
        String cleanedJson = response.replaceAll("^```json|```$", "").trim();
        JSONOBJECT = new JSONObject(cleanedJson);

        // category 객체 가져오기
        int category = JSONOBJECT.getInt("category");
        System.out.println("Category: " + category);

        return category;
    }

    // response_message 반환. categorizeChatGPT 까지 함수 실행된 상태에서만 호출해야 정상기능.
    public String chatGPTResponse() {
        System.out.print("response_message 출력: ");
        return JSONOBJECT.getString("response_message");
    }

    public ChatGPTRequestGetResponse getChatGPTRequest(ChatGPTRequestGetRequest request) { // 몇 번 category?
        /*
         * TODO: GPT에 요청 보내서 요청 타입 분류하기 (완)
         *
         * request.requestId:       요청 번호
         * request.requestContent:  사용자 요청
         */
        int category = 1;
        String response = "", city = "", startDt = "", endDt = "";

        try {
            String requestId_string = request.requestId();
            REQUESTID = Integer.parseInt(requestId_string);

            processChatGPT(request.requestContent());
            category = categorizeChatGPT(RESPONSE);
            response = JSONOBJECT.getString("response_message");
            // System.out.println("\n\nresponse : " + response);
            city = JSONOBJECT.getJSONObject("content").getString("travel_city");
            // System.out.println("\n\ncity : " + city);
            startDt = JSONOBJECT.getJSONObject("content").getString("travel_start_date");
            // System.out.println("\n\nstartDt : " + startDt);
            endDt = JSONOBJECT.getJSONObject("content").getString("travel_end_date");
            // System.out.println("\n\nendDt : " + endDt);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred: " + e.getMessage());
        }

        return new ChatGPTRequestGetResponse(REQUESTID, category, city, startDt, endDt, response);
    }

    public ChatGPTResponseGetResponse getChatGPTResponseId(String requestId) {
        /*
         * TODO: GPT 응답(계획 외 정보) 가져오기 (완)
         *
         * requestId: 요청 번호
         */
        return new ChatGPTResponseGetResponse(chatGPTResponse());
    }

    public List<ChatGPTPlanGetResponse> getChatGPTPlanList(String requestId) {
        /*
         * TODO: GPT 여행 계획 가져오기 (완)
         *
         * requestId: 요청 번호
         */
        return getDummyChatGPTPlanList();
    }

    // 여행 계획 더미 데이터
    public List<ChatGPTPlanGetResponse> getDummyChatGPTPlanList() {
        List<ChatGPTPlanGetResponse> chatGPTPlanList = new ArrayList<>();

        try {
            JSONOBJECT.getJSONObject("content");

            // content 객체 가져오기
            JSONObject content = JSONOBJECT.getJSONObject("content");

            String city = content.getString("travel_city");
            DAYS = content.getInt("travel_days");

            // attraction_list에서 각 요소의 name, time, detail 값을 배열로 받기
            JSONArray attractionList = content.getJSONArray("attraction_list");
            List<String> attractionNames = new ArrayList<>();
            List<String> attractionTimes = new ArrayList<>();
            List<String> attractionDetails = new ArrayList<>();
            for (int i = 0; i < attractionList.length(); i++) {
                JSONObject attraction = attractionList.getJSONObject(i);
                attractionNames.add(attraction.getString("name"));
                attractionTimes.add(attraction.getString("time"));
                attractionDetails.add(attraction.getString("detail"));
            }
            System.out.println("Attractions: " + attractionNames);
            System.out.println("Attractions Time: " + attractionTimes);
            System.out.println("Attraction Details: " + attractionDetails);
            placeNameList = attractionNames;

            // restaurant_list에서 각 요소의 name과 detail 값을 배열로 받기
            JSONArray restaurantList = content.getJSONArray("restaurant_list");
            List<String> restaurantNames = new ArrayList<>();
            List<String> restaurantTimes = new ArrayList<>();
            List<String> restaurantDetails = new ArrayList<>();
            for (int i = 0; i < restaurantList.length(); i++) {
                JSONObject restaurant = restaurantList.getJSONObject(i);
                restaurantNames.add(restaurant.getString("name"));
                restaurantTimes.add(restaurant.getString("time"));
                restaurantDetails.add(restaurant.getString("detail"));
            }
            System.out.println("Restaurants: " + restaurantNames);
            System.out.println("Restaurants Time: " + restaurantTimes);
            System.out.println("Restaurant Details: " + restaurantDetails);

            Iterator<String> attrNmIt = attractionNames.iterator();
            Iterator<String> attrTmIt = attractionTimes.iterator();
            Iterator<String> attrDtIt = attractionDetails.iterator();
            Iterator<String> restNmIt = restaurantNames.iterator();
            Iterator<String> restTmIt = restaurantTimes.iterator();
            Iterator<String> restDtIt = restaurantDetails.iterator();

            for (int i = 0; i < DAYS; i++) {
                chatGPTPlanList.add(
                        ChatGPTPlanGetResponse.builder()
                                .city(city)
                                .day("Day" + (i + 1))
                                .travel_destination_1(attrNmIt.next())
                                .travel_destination_1_time(attrTmIt.next())
                                .travel_destination_1_detail(attrDtIt.next())
                                .travel_destination_2(attrNmIt.next())
                                .travel_destination_2_detail(attrDtIt.next())
                                .travel_destination_2_time(attrTmIt.next())
                                .travel_destination_3(attrNmIt.next())
                                .travel_destination_3_time(attrTmIt.next())
                                .travel_destination_3_detail(attrDtIt.next())
                                .restaurant_1(restNmIt.next())
                                .restaurant_1_time(restTmIt.next())
                                .restaurant_1_detail(restDtIt.next())
                                .restaurant_2(restNmIt.next())
                                .restaurant_2_time(restTmIt.next())
                                .restaurant_2_detail(restDtIt.next())
                                .restaurant_3(restNmIt.next())
                                .restaurant_3_time(restTmIt.next())
                                .restaurant_3_detail(restDtIt.next())
                                .build()
                );

                chatGPTPlanListCpy = chatGPTPlanList;
            }

//            System.out.println("chatGPTPlanList : " + chatGPTPlanList);
            System.out.println("chatGPTPlanList : " + chatGPTPlanListCpy);

        } catch (JSONException e) {
            System.out.println("정확하지 않은 JSON 반환");
            e.printStackTrace();
            return null;
        }

//        return chatGPTPlanList;
        return chatGPTPlanListCpy;
    }

//    public String changePlaceNameList() {
//        String before = JSONOBJECT.getJSONObject("content").getString("previous_name");
//        String after = JSONOBJECT.getJSONObject("content").getString("name");
//        String detail = JSONOBJECT.getJSONObject("content").getString("detail");
//
//        int bfIdx = placeNameList.indexOf(before);
//        placeNameList.set(bfIdx, after);
//
//        return String.join(", ", placeNameList);
//    }

    public String changePlaceNameList() {
        String before = JSONOBJECT.getJSONObject("content").getString("previous_name");
        String after = JSONOBJECT.getJSONObject("content").getString("name");
        String detail = JSONOBJECT.getJSONObject("content").getString("detail");

        int bfIdx = placeNameList.indexOf(before);
        if (bfIdx != -1) {
            placeNameList.set(bfIdx, after);
        }

        List<ChatGPTPlanGetResponse> updatedPlanList = new ArrayList<>();
        for (ChatGPTPlanGetResponse plan : chatGPTPlanListCpy) {
            ChatGPTPlanGetResponse updatedPlan = plan;

            // 각 필드를 조건에 따라 새롭게 교체된 인스턴스를 만듦
            if (plan.travel_destination_1().equals(before)) {
                updatedPlan = new ChatGPTPlanGetResponse(
                        plan.city(),
                        plan.day(),
                        after,
                        plan.travel_destination_1_time(),
                        detail,
                        plan.travel_destination_2(),
                        plan.travel_destination_2_time(),
                        plan.travel_destination_2_detail(),
                        plan.travel_destination_3(),
                        plan.travel_destination_3_time(),
                        plan.travel_destination_3_detail(),
                        plan.restaurant_1(),
                        plan.restaurant_1_time(),
                        plan.restaurant_1_detail(),
                        plan.restaurant_2(),
                        plan.restaurant_2_time(),
                        plan.restaurant_2_detail(),
                        plan.restaurant_3(),
                        plan.restaurant_3_time(),
                        plan.restaurant_3_detail()
                );
            } else if (plan.travel_destination_2().equals(before)) {
                updatedPlan = new ChatGPTPlanGetResponse(
                        plan.city(),
                        plan.day(),
                        plan.travel_destination_1(),
                        plan.travel_destination_1_time(),
                        plan.travel_destination_1_detail(),
                        after,
                        plan.travel_destination_2_time(),
                        detail,
                        plan.travel_destination_3(),
                        plan.travel_destination_3_time(),
                        plan.travel_destination_3_detail(),
                        plan.restaurant_1(),
                        plan.restaurant_1_time(),
                        plan.restaurant_1_detail(),
                        plan.restaurant_2(),
                        plan.restaurant_2_time(),
                        plan.restaurant_2_detail(),
                        plan.restaurant_3(),
                        plan.restaurant_3_time(),
                        plan.restaurant_3_detail()
                );
            } else if (plan.travel_destination_3().equals(before)) {
                updatedPlan = new ChatGPTPlanGetResponse(
                        plan.city(),
                        plan.day(),
                        plan.travel_destination_1(),
                        plan.travel_destination_1_time(),
                        plan.travel_destination_1_detail(),
                        plan.travel_destination_2(),
                        plan.travel_destination_2_time(),
                        plan.travel_destination_2_detail(),
                        after,
                        plan.travel_destination_3_time(),
                        detail,
                        plan.restaurant_1(),
                        plan.restaurant_1_time(),
                        plan.restaurant_1_detail(),
                        plan.restaurant_2(),
                        plan.restaurant_2_time(),
                        plan.restaurant_2_detail(),
                        plan.restaurant_3(),
                        plan.restaurant_3_time(),
                        plan.restaurant_3_detail()
                );
            }
            updatedPlanList.add(updatedPlan);
        }

        chatGPTPlanListCpy = updatedPlanList;

        System.out.println("chatGPTPlanListCpy : " + chatGPTPlanListCpy);

        return String.join(", ", placeNameList);
    }


}