package com.haandy.travel_planner_server.domain.chatGPT.controller;

import com.haandy.travel_planner_server.domain.chatGPT.dto.request.ChatGPTRequestGetRequest;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTPlanGetResponse;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTRequestGetResponse;
import com.haandy.travel_planner_server.domain.chatGPT.dto.response.ChatGPTResponseGetResponse;
import com.haandy.travel_planner_server.domain.chatGPT.service.ChatGPTService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chatgpt")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

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
        return ResponseEntity.ok(chatGPTService.getChatGPTResponse(requestId));
    }

    @GetMapping("/travel-plan/{requestId}")
    @Operation(summary = "GPT 여행 계획 가져오기")
    public ResponseEntity<List<ChatGPTPlanGetResponse>> getChatGPTPlan(
            @PathVariable("requestId") String requestId
    ) {
        return ResponseEntity.ok(chatGPTService.getChatGPTPlanList(requestId));
    }
}
