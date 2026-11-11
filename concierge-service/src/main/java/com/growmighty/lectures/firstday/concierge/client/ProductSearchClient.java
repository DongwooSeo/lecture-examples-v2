package com.growmighty.lectures.firstday.concierge.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/** R(etrieval) — product-service 의 하이브리드 검색으로 질문과 관련된 상품을 가져온다 (논블로킹 WebClient) */
@Component
public class ProductSearchClient {

    private final WebClient webClient;
    private final int topK;

    public ProductSearchClient(WebClient.Builder builder,
                               @Value("${concierge.product-base-url}") String baseUrl,
                               @Value("${concierge.top-k:5}") int topK) {
        this.webClient = builder.baseUrl(baseUrl).build();
        this.topK = topK;
    }

    public Mono<List<CandidateProduct>> searchCandidates(String question) {
        return webClient.get()
                .uri(uri -> uri.path("/products/search/hybrid")
                        .queryParam("keyword", question)
                        .queryParam("size", topK)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Envelope>() {
                })
                .map(envelope -> envelope.data() == null ? List.<CandidateProduct>of() : envelope.data());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Envelope(boolean success, List<CandidateProduct> data) {
    }
}
