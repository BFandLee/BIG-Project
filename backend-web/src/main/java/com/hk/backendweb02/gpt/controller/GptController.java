package com.hk.backendweb02.gpt.controller;

import com.hk.backendweb02.gpt.dto.GptDto;
import com.hk.backendweb02.gpt.service.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gpt")
public class GptController {

    private final GptService gptService;

    @PostMapping("/ask")
    public ResponseEntity<GptDto.GptResponse> askGpt(@RequestBody GptDto.QuestionRequest request) {
        String answer = gptService.getGptResponse(request.getPrompt());
        GptDto.GptResponse response = new GptDto.GptResponse();
        response.setAnswer(answer);
        return ResponseEntity.ok(response);
    }
}