package com.growmighty.lectures.firstday.concierge.presentation;

import com.growmighty.lectures.firstday.concierge.application.ConciergeAnswer;
import com.growmighty.lectures.firstday.concierge.application.ConciergeService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/concierge")
public class ConciergeController {

    private final ConciergeService conciergeService;

    public ConciergeController(ConciergeService conciergeService) {
        this.conciergeService = conciergeService;
    }

    public record AskRequest(String question) {
    }

    /** 다 만들어질 때까지 기다렸다가 한 번에 — Gateway 타임아웃(504)에 걸리기 쉽다 */
    @PostMapping("/ask")
    public Mono<ConciergeAnswer> ask(@RequestBody AskRequest request) {
        return conciergeService.ask(request.question());
    }

    /**
     * SSE — ① sources 이벤트(근거 상품 ID 목록) → ② token 이벤트들 → ③ done 이벤트.
     * 검색은 한 번만 하고, 같은 근거로 생성한다.
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> stream(@RequestParam String question) {
        return conciergeService.retrieve(question)
                .flatMapMany(candidates -> {
                    String sourceIds = candidates.stream()
                            .map(c -> String.valueOf(c.id()))
                            .collect(Collectors.joining(","));
                    Flux<ServerSentEvent<String>> sources =
                            Flux.just(ServerSentEvent.<String>builder(sourceIds).event("sources").build());
                    Flux<ServerSentEvent<String>> tokens = conciergeService.generate(question, candidates)
                            .map(token -> ServerSentEvent.<String>builder(token).event("token").build());
                    return sources.concatWith(tokens);
                })
                .concatWith(Flux.just(ServerSentEvent.<String>builder("").event("done").build()));
    }
}
