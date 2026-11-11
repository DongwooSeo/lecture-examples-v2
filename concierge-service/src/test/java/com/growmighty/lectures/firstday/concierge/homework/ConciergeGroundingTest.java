package com.growmighty.lectures.firstday.concierge.homework;

import com.growmighty.lectures.firstday.concierge.application.ConciergeAnswer;
import com.growmighty.lectures.firstday.concierge.application.ConciergeService;
import com.growmighty.lectures.firstday.concierge.client.CandidateProduct;
import com.growmighty.lectures.firstday.concierge.client.ProductSearchClient;
import com.growmighty.lectures.firstday.concierge.presentation.ConciergeController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.RecordComponent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * [과제 AI-2 검증 테스트] 컨시어지 답변에 "근거 상품"이 함께 나가는지 확인합니다. 실제 LLM·검색 없이 돕니다.
 *
 * 1. ConciergeAnswer 에 sources 필드(근거 상품 목록, 각 항목에 id)를 추가하세요.
 * 2. /concierge/stream 은 토큰보다 먼저 "sources" 이벤트를 한 번 보내세요.
 */
class ConciergeGroundingTest {

    private final ChatModel chatModel = mock(ChatModel.class);
    private final ProductSearchClient searchClient = mock(ProductSearchClient.class);
    private ConciergeService service;

    @BeforeEach
    void setUp() {
        when(chatModel.getOptions()).thenReturn(ChatOptions.builder().build());
        when(chatModel.call(any(Prompt.class))).thenReturn(response("[상품 1] 새틴 슬립 드레스를 추천해요."));
        when(chatModel.stream(any(Prompt.class))).thenReturn(Flux.just(response("[상품 1] "), response("추천해요.")));
        service = new ConciergeService(ChatClient.builder(chatModel), searchClient);
    }

    @Test
    @DisplayName("/ask 응답에 LLM 에게 근거로 넘긴 상품들이 sources 로 함께 담긴다")
    void askIncludesSources() throws Exception {
        when(searchClient.searchCandidates(anyString())).thenReturn(Mono.just(List.of(candidate(1L), candidate(2L))));

        ConciergeAnswer answer = service.ask("하객룩 원피스").block();

        assertThat(sourceIds(answer)).containsExactly(1L, 2L);
    }

    @Test
    @DisplayName("검색된 상품이 없으면 sources 도 비어 있다")
    void noCandidatesNoSources() throws Exception {
        when(searchClient.searchCandidates(anyString())).thenReturn(Mono.just(List.of()));

        ConciergeAnswer answer = service.ask("존재하지 않는 상품").block();

        assertThat(sourceIds(answer)).isEmpty();
    }

    @Test
    @DisplayName("스트리밍은 첫 이벤트로 sources 를 보낸 뒤 토큰을 흘려보낸다")
    void streamSendsSourcesFirst() {
        when(searchClient.searchCandidates(anyString())).thenReturn(Mono.just(List.of(candidate(1L))));
        ConciergeController controller = new ConciergeController(service);

        List<ServerSentEvent<String>> events = controller.stream("하객룩 원피스").collectList().block();

        assertThat(events).isNotEmpty();
        assertThat(events.get(0).event()).isEqualTo("sources");
        assertThat(events.get(0).data()).contains("1");
        assertThat(events).extracting(ServerSentEvent::event).contains("token", "done");
    }

    /** ConciergeAnswer.sources() 를 리플렉션으로 읽는다 (필드를 추가하기 전에도 컴파일되도록) */
    private static List<Long> sourceIds(ConciergeAnswer answer) throws Exception {
        RecordComponent sources = Arrays.stream(ConciergeAnswer.class.getRecordComponents())
                .filter(c -> c.getName().equals("sources"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("ConciergeAnswer 에 sources 필드를 추가하세요"));
        List<Long> ids = new ArrayList<>();
        for (Object item : (List<?>) sources.getAccessor().invoke(answer)) {
            RecordComponent id = Arrays.stream(item.getClass().getRecordComponents())
                    .filter(c -> c.getName().equals("id"))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("sources 의 각 항목에 id 가 있어야 합니다"));
            ids.add((Long) id.getAccessor().invoke(item));
        }
        return ids;
    }

    private static CandidateProduct candidate(Long id) {
        return new CandidateProduct(id, "상품" + id, "설명", BigDecimal.valueOf(89_000), List.of("결혼식"), List.of("우아한"), "새틴");
    }

    private static ChatResponse response(String text) {
        return new ChatResponse(List.of(new Generation(new AssistantMessage(text))));
    }
}
