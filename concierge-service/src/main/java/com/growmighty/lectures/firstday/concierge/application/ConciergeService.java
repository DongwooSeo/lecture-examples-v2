package com.growmighty.lectures.firstday.concierge.application;

import com.growmighty.lectures.firstday.concierge.client.CandidateProduct;
import com.growmighty.lectures.firstday.concierge.client.ProductSearchClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RAG = R(검색) → A(컨텍스트 구성) → G(생성)
 * 상품 목록에 "없는" 상품을 지어내지 않도록, 근거로 넘긴 상품 안에서만 답하게 한다.
 */
@Service
public class ConciergeService {

    private static final String SYSTEM_PROMPT = """
            당신은 쇼핑몰의 상품 추천 컨시어지입니다.
            반드시 [상품 목록] 안의 상품만 추천하세요. 목록에 없는 상품·가격·사양을 지어내면 안 됩니다.
            추천할 때는 상품 이름 옆에 [상품 번호]를 함께 적으세요.
            목록에 질문에 맞는 상품이 없으면 "조건에 맞는 상품을 찾지 못했어요"라고 답하세요.
            답변은 한국어로 3~5문장.
            """;

    private final ChatClient chatClient;
    private final ProductSearchClient searchClient;

    public ConciergeService(ChatClient.Builder builder, ProductSearchClient searchClient) {
        this.chatClient = builder.defaultSystem(SYSTEM_PROMPT).build();
        this.searchClient = searchClient;
    }

    /** R — 질문과 관련된 상품(근거)을 가져온다 */
    public Mono<List<CandidateProduct>> retrieve(String question) {
        return searchClient.searchCandidates(question);
    }

    /** 한 번에 받는 답변 — 생성이 끝날 때까지 기다린다. 근거 상품(sources)을 함께 돌려준다 */
    public Mono<ConciergeAnswer> ask(String question) {
        return retrieve(question)
                .flatMap(candidates -> Mono.fromCallable(() -> chatClient.prompt()
                                .user(userPrompt(question, candidates))
                                .call()
                                .content())
                        .subscribeOn(Schedulers.boundedElastic())   // 블로킹 호출은 이벤트 루프 밖에서
                        .map(answer -> new ConciergeAnswer(question, answer, ConciergeAnswer.sourcesOf(candidates))));
    }

    /** 토큰 단위 스트리밍 — 근거를 이미 가져온 경우 */
    public Flux<String> generate(String question, List<CandidateProduct> candidates) {
        return chatClient.prompt()
                .user(userPrompt(question, candidates))
                .stream()
                .content();
    }

    /** 토큰 단위 스트리밍 — 검색부터 한 번에 */
    public Flux<String> stream(String question) {
        return retrieve(question).flatMapMany(candidates -> generate(question, candidates));
    }

    static String userPrompt(String question, List<CandidateProduct> candidates) {
        String context = candidates.isEmpty()
                ? "(검색된 상품 없음)"
                : candidates.stream().map(CandidateProduct::toContextLine).collect(Collectors.joining("\n"));
        return """
                [상품 목록]
                %s

                [고객 질문]
                %s
                """.formatted(context, question);
    }
}
