package com.haandy.travel_planner_server.domain.chatGPT.service;

import com.haandy.travel_planner_server.domain.chatGPT.dto.request.ChatGPTRequestGetRequest;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTPlanGetResponse;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTRequestGetResponse;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTResponseGetResponse;
import lombok.RequiredArgsConstructor;
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

    private List<String[]> travelListCpy, restaurantListCpy, accommodationListCpy;

    // ChatGPT와의 통신을 통해 메시지 결과를 받아옴
    public String getChatGPTResponse(String message) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("travel_planner_env/bin/python3", "python/chatgpt.py", message);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        processChatGPTResponse(result.toString());

        // 응답을 문자열 형태로 반환
        return result.toString();
    }

    // 받아온 chatGPT 메시지 결과를 카테고리에 맞게 추출 함수를 호출
    public void processChatGPTResponse(String response) {

        if (response.contains("카테고리 번호: 1")) {
            extractData1(response);
        }
        if (response.contains("카테고리 번호: 2")) {
//            extractData2(response);
        }
        if (response.contains("카테고리 번호: 3")) {
//            extractData3(response);
        }
        if (response.contains("카테고리 번호: 4")) {
//            extractData4(response);
        }
        if (response.contains("카테고리 번호: 5")) {
//            extractData5(response);
        }

    }

    private void extractData1(String response) {
        // 정규 표현식
        String cityPattern = "여행도시:\\s*(\\S+)";
        String datePattern = "여행날짜:\\s*(\\d{2}/\\d{2})~(\\d{2}/\\d{2})";
        String travelPattern = "여행지\\[(.*?)\\]";
        String restaurantPattern = "식당\\[(.*?)\\]";
        String accommodationPattern = "숙소\\[(.*?)\\]";

        // 장소와 시간 추출 패턴 (여행지, 식당, 숙소)
        String travelTimePattern = "([가-힣\\s\\w]+)\\((\\d+)(일)\\s*(\\d{2}:\\d{2})~(\\d{2}:\\d{2})\\)";
        String restaurantTimePattern = "([가-힣\\s\\w]+)\\((\\d+)(일)\\s*(\\d{2}:\\d{2})~(\\d{2}:\\d{2})\\)";
        String accommodationTimePattern = "([가-힣\\s\\w]+)\\((\\d+)(일)\\)";

        // 여행 도시
        Matcher cityMatcher = Pattern.compile(cityPattern).matcher(response);
        if (cityMatcher.find()) {
            String city = cityMatcher.group(1);
            System.out.println("여행도시 : " + city); //출력
        }

        // 여행 날짜
        Matcher dateMatcher = Pattern.compile(datePattern).matcher(response);
        if (dateMatcher.find()) {
            String startDate = dateMatcher.group(1);
            String endDate = dateMatcher.group(2);
            System.out.println("시작날짜 : " + startDate + ", 종료날짜 : " + endDate); //출력
        }

        // 여행지
        Matcher travelMatcher = Pattern.compile(travelPattern).matcher(response);
        if (travelMatcher.find()) {
            String travelPlaces = travelMatcher.group(1);
            Matcher placeTimeMatcher = Pattern.compile(travelTimePattern).matcher(travelPlaces);
            List<String[]> travelList = new ArrayList<>();

            while (placeTimeMatcher.find()) {
                String place = placeTimeMatcher.group(1).trim();
                String date = placeTimeMatcher.group(2);  // 날짜 추출
                String startTime = placeTimeMatcher.group(4); // 시작 시간
                String endTime = placeTimeMatcher.group(5);   // 종료 시간
                travelList.add(new String[]{place, date, startTime, endTime});
            }

            travelListCpy = new ArrayList<>(travelList);

            System.out.println("--여행지--"); //출력
            for (String[] travel : travelList) {
                System.out.println("장소: " + travel[0] + ", 날짜(일): " + travel[1] + ", 시작시간: " + travel[2] + ", 종료시간: " + travel[3]);
            }
        }

        // 식당
        Matcher restaurantMatcher = Pattern.compile(restaurantPattern).matcher(response);
        if (restaurantMatcher.find()) {
            String restaurants = restaurantMatcher.group(1);
            Matcher restaurantPlaceTimeMatcher = Pattern.compile(restaurantTimePattern).matcher(restaurants);
            List<String[]> restaurantList = new ArrayList<>();

            while (restaurantPlaceTimeMatcher.find()) {
                String place = restaurantPlaceTimeMatcher.group(1).trim();
                String date = restaurantPlaceTimeMatcher.group(2);  // 날짜 추출
                String startTime = restaurantPlaceTimeMatcher.group(4); // 시작 시간
                String endTime = restaurantPlaceTimeMatcher.group(5);   // 종료 시간
                restaurantList.add(new String[]{place, date, startTime, endTime});
            }

            restaurantListCpy = new ArrayList<>(restaurantList);

            System.out.println("--식당--"); //출력
            for (String[] restaurant : restaurantList) {
                System.out.println("장소: " + restaurant[0] + ", 날짜(일): " + restaurant[1] + ", 시작시간: " + restaurant[2] + ", 종료시간: " + restaurant[3]);
            }
        }

        // 숙소
        Matcher accommodationMatcher = Pattern.compile(accommodationPattern).matcher(response);
        if (accommodationMatcher.find()) {
            String accommodations = accommodationMatcher.group(1);
            Matcher accommodationMatcherWithDate = Pattern.compile(accommodationTimePattern).matcher(accommodations);
            List<String[]> accommodationList = new ArrayList<>();

            while (accommodationMatcherWithDate.find()) {
                String accommodation = accommodationMatcherWithDate.group(1).trim();
                String date = accommodationMatcherWithDate.group(2); // 날짜 추출
                accommodationList.add(new String[]{accommodation, date});
            }

            accommodationListCpy = new ArrayList<>(accommodationList);

            System.out.println("--숙소--"); //출력
            for (String[] accommodation : accommodationList) {
                System.out.println("숙소: " + accommodation[0] + ", 날짜(일): " + accommodation[1]);
            }
        }

        Iterator<String[]> trvlIt = travelListCpy.iterator();
        Iterator<String[]> rstrIt = restaurantListCpy.iterator();
        Iterator<String[]> accmIt = accommodationListCpy.iterator();

        System.out.println("***** trvlIt *****");
        while (trvlIt.hasNext()) System.out.println(trvlIt.next()[0]);
        System.out.println("***** rstrIt *****");
        while (rstrIt.hasNext()) System.out.println(rstrIt.next()[0]);
        System.out.println("***** accmIt *****");
        while (accmIt.hasNext()) System.out.println(accmIt.next()[0]);

    }

    public ChatGPTRequestGetResponse getChatGPTRequest(ChatGPTRequestGetRequest request) {
        /**
         * TODO: GPT에 요청 보내서 요청 타입 분류하기
         *
         * request.requestId:       요청 번호
         * request.requestContent:  사용자 요청
         */

        return new ChatGPTRequestGetResponse(1, 1, "Response from chatGPT");
    }

    public ChatGPTResponseGetResponse getChatGPTResponseId(String requestId) {

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

        Iterator<String[]> trvlIt = travelListCpy.iterator();
        Iterator<String[]> rstrIt = restaurantListCpy.iterator();
//        Iterator<String[]> accmIt = accommodationListCpy.iterator();
        int day = 1;

        // (총 날짜 수 / 3) 만큼 for문으로 반복
        chatGPTPlanList.add(
                    ChatGPTPlanGetResponse.builder()
                            .day("Day" + (day++))
                            .travel_destination_1(trvlIt.next()[0])
                            .travel_destination_2(trvlIt.next()[0])
                            .travel_destination_3(trvlIt.next()[0])
                            .restaurant_1(rstrIt.next()[0])
                            .restaurant_2(rstrIt.next()[0])
                            .restaurant_3(rstrIt.next()[0])
                            .build()
        );

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
