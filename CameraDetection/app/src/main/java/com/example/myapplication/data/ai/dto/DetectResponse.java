// data/ai/dto/DetectResponse.java
package com.example.myapplication.data.ai.dto;
import java.util.List;

/**
 * FastAPI /detect 응답 스키마와 1:1 매핑.
 * 서버 JSON 키와 이름/타입이 정확히 맞아야 함.
 */
public class DetectResponse {
    // 예: ["egg","beef"]
    public List<String> ingredients;

    // 예: [{"name":"egg","confidence":0.97,"bbox":[251,397,599,808]}]
    public List<Detection> detections;

    public static class Detection {
        public String name;         // 감지 클래스명
        public double confidence;   // 신뢰도(0~1)
        public List<Integer> bbox;  // 바운딩박스 [x1,y1,x2,y2] (서버가 float면 List<Double>로 변경)
    }
}
