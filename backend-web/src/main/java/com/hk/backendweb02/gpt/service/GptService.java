package com.hk.backendweb02.gpt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException; // import 추가
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;
import java.util.List;

@Service
public class GptService {

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public String getGptResponse(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(OPENAI_API_URL, HttpMethod.POST, entity, Map.class);

            Map<String, Object> responseBody = response.getBody();
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

            return (String) message.get("content");
        } catch (HttpClientErrorException e) {
            // OpenAI가 보낸 구체적인 에러 메시지를 백엔드 콘솔에 출력
            System.err.println("OpenAI API Error: " + e.getStatusCode() + " " + e.getResponseBodyAsString());
            return "GPT 응답 실패: " + "크레딧을 충전하세요"; // 프론트엔드에는 상태 코드만 알려줌
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            return "GPT 응답을 가져오는 데 실패했습니다.";
        }
    }
}