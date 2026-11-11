package com.growmighty.lectures.firstday.concierge.presentation;

import com.growmighty.lectures.firstday.concierge.application.ConciergeAnswer;
import com.growmighty.lectures.firstday.concierge.application.ConciergeService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    /** SSE — 토큰이 만들어지는 대로 흘려보낸다. 마지막에 done 이벤트 */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> stream(@RequestParam String question) {
        Flux<ServerSentEvent<String>> tokens = conciergeService.stream(question)
                .map(token -> ServerSentEvent.<String>builder(token).event("token").build());
        Flux<ServerSentEvent<String>> done = Flux.just(ServerSentEvent.<String>builder("").event("done").build());
        return tokens.concatWith(done);
    }
}
