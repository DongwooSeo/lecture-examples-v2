package com.growmighty.lectures.firstday.concierge.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

/** product-service 하이브리드 검색 결과 중 LLM 근거로 쓸 필드만 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CandidateProduct(
        Long id,
        String name,
        String description,
        BigDecimal price,
        List<String> occasions,
        List<String> styles,
        String material
) {
    /** 프롬프트에 넣을 한 줄 요약 */
    public String toContextLine() {
        return "[상품 %d] %s | %s원 | 상황:%s | 스타일:%s | 소재:%s | %s".formatted(
                id, name, price, occasions, styles, material, description);
    }
}
