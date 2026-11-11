package com.growmighty.lectures.firstday.concierge.application;

import com.growmighty.lectures.firstday.concierge.client.CandidateProduct;

import java.util.List;

/**
 * @param sources LLM 에게 근거로 넘긴 상품들 — 사용자가 답변을 직접 검증할 수 있게 함께 돌려준다
 */
public record ConciergeAnswer(String question, String answer, List<Source> sources) {

    public record Source(Long id, String name) {
        public static Source from(CandidateProduct product) {
            return new Source(product.id(), product.name());
        }
    }

    public static List<Source> sourcesOf(List<CandidateProduct> candidates) {
        return candidates.stream().map(Source::from).toList();
    }
}
