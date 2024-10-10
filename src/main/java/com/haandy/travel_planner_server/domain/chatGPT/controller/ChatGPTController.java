package com.haandy.travel_planner_server.domain.chatGPT.controller;

import com.haandy.travel_planner_server.domain.chatGPT.dto.request.ChatGPTRequestGetRequest;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTPlanGetResponse;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTRequestGetResponse;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTResponseGetResponse;
import com.haandy.travel_planner_server.domain.chatGPT.service.ChatGPTService;
import io.swagger.v3.oas.annotations.Operation;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

import java.util.ArrayList;
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

    // 테스트 코드
    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        System.out.println("/*** chatGPTService.processChatGPT(message); ***/");
        String tmp = chatGPTService.processChatGPT(message);

        System.out.println("/*** chatGPTService.categorizeChatGPT(tmp); ***/");
        chatGPTService.categorizeChatGPT(tmp);

        System.out.println("/*** chatGPTService.getChatGPTPlanList(\"1\"); ***/");
        chatGPTService.getChatGPTPlanList("1");

        return "";
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
