package com.hk.backendweb02.gpt.dto;

import lombok.Getter;
import lombok.Setter;

public class GptDto {

    @Getter
    @Setter
    public static class QuestionRequest {
        private String prompt;
    }

    @Getter
    @Setter
    public static class GptResponse {
        private String answer;
    }
}