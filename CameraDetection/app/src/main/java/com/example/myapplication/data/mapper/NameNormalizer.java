package com.example.myapplication.data.mapper;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class NameNormalizer {
    private NameNormalizer(){}

    // 간단한 동의어/번역 맵 (필요 시 계속 추가)
    private static final Map<String, String> CANON = new HashMap<>();
    static {
        // 영어 -> 한글
        CANON.put("egg", "계란");
        CANON.put("eggs", "계란");
        CANON.put("beef", "고기");
        CANON.put("meat", "고기");
        // 한글 변형
        CANON.put("달걀", "계란");
        CANON.put("소고기", "고기");
    }

    /** 비교용 표준 이름으로 변환 */
    public static String canon(String raw) {
        if (raw == null) return null;
        String s = raw.trim().toLowerCase(Locale.ROOT);
        // 공백/구두점 제거 등 필요시 추가 전처리
        s = s.replaceAll("\\s+", "");
        // 사전 매핑
        String mapped = CANON.get(s);
        if (mapped != null) return mapped;
        // 영어인 경우 그대로 두기보다, 여기서는 소문자 원형 반환
        // (DB가 한글이면 사실상 사전 매핑으로 커버)
        // 한글은 공백 제거된 원문을 다시 사용
        return raw.trim();
    }
}
