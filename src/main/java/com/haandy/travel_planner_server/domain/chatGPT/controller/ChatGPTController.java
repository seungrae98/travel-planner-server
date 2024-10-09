package com.haandy.travel_planner_server.domain.chatGPT.controller;

import com.haandy.travel_planner_server.domain.chatGPT.dto.request.ChatGPTRequestGetRequest;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTPlanGetResponse;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTRequestGetResponse;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTResponseGetResponse;
import com.haandy.travel_planner_server.domain.chatGPT.service.ChatGPTService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chatgpt")
@CrossOrigin(origins = "http://localhost:3000")
//@RequiredArgsConstructor
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    @Autowired
    public ChatGPTController(ChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }

    // /chat 엔드포인트에서 ChatGPT의 응답을 문자열로 반환
    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        try {
            // ChatGPTService에서 처리된 응답을 문자열로 받아옴
            System.out.println("User Message: \n" + message);

            String prefix = "너는 내 여행 계획 비서이며, 나에게 여행 계획을 짜줘야해. 나는 네 여행 계획을 참고해서 여행을 할거야.\n";
            String suffix = """         
                    \n사용자의 문장을 분석해서 JSON 파일 형식으로 출력해.
                      
                    모든 출력 필수 키값은
                    { “category”, “response_message”, “content” }
                    이며 아래 요구사항에 맞게 출력해
                      
                    category: 사용자 문장의 목적이 아래 카테고리 중 어디에 해당하는지 분석하여 번호 만을 출력해
                    1. 여행 계획 추천
                    2. 여행 일정 변경
                    3. 여행지 정보 검색
                    4. 일정 간 경로 검색
                    5. 해당 없음
                      
                    response_message: 사용자 문장에 대한 너의 상세한 답변. 그것이 만약
                    1번 카테고리일 경우: 여행도시와 여행날짜 정보가 명확하지 않다면 다시 물어보고, 명확하다면 그에 맞는 content에 들어갈 상세한 계획을 자연어 형식으로 출력해.
                    5번 카테고리일 경우: 여행 계획과 관련된 질문을 하라고 출력해.
                      
                    content: 카테고리마다 아래와 같이 다른 키값을 하위에 출력해.
                    1번 카테고리일 경우:
                    {
                    	travel_city,
                    	travel_start_date: MM/dd,
                      	travel_end_date: MM/dd,
                      	attraction_list: 추천 관광지,
                      	restaurant_list: 추천 식당,
                      	accommodation_list: 추천 숙소
                    }
                    단, attraction_list, restaurant_list, accommodation_list는 아래에 키값을 배열로 나열해
                    {
                    	name: 장소 이름,
                    	date: dd,
                    	start_time: hh:mm,
                    	end_time: hh:mm,
                    	detail: 추천이유 혹은 장소 설명
                    }
                    
                    그리고 관광지와 식당은 반드시 하루에 최소 3개 이상씩 추천해줘.
                    
                    2, 3, 4, 5번 카테고리일 경우 값은 0이야.
                    """;

            message = prefix + '\n' + message + suffix;
            String response = chatGPTService.getChatGPTResponse(message);
            System.out.println("ChatGPT Response: \n" + response + "\n===================================");

            return chatGPTService.getChatGPTResponse(message);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }

    @PostMapping("/request")
    @Operation(summary = "GPT에 사용자 요청 생성하기")
    public ResponseEntity<ChatGPTRequestGetResponse> postChatGPTRequest(
            @RequestBody ChatGPTRequestGetRequest request
    ) {
        System.out.println("request: " + request.requestContent());
        return ResponseEntity.ok(chatGPTService.getChatGPTRequest(request));
    }

    @GetMapping("/response/{requestId}")
    @Operation(summary = "GPT 응답 가져오기")
    public ResponseEntity<ChatGPTResponseGetResponse> getChatGPTResponse(
            @PathVariable("requestId") String requestId
    ) {
        return ResponseEntity.ok(chatGPTService.getChatGPTResponseId(requestId));
    }

    @GetMapping("/travel-plan/{requestId}")
    @Operation(summary = "GPT 여행 계획 가져오기")
    public ResponseEntity<List<ChatGPTPlanGetResponse>> getChatGPTPlan(
            @PathVariable("requestId") String requestId
    ) {
        return ResponseEntity.ok(chatGPTService.getChatGPTPlanList(requestId));
    }
}
