package com.growmighty.lectures.firstday.cart.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * [과제 MSA-2 검증 테스트] cart-service 의 상품 호출이 RestClient 손코딩에서 FeignClient 로 바뀌었는지 소스를 읽어 확인합니다.
 * 서킷 브레이커(Fallback)는 그대로 살아 있어야 합니다.
 */
class CartFeignMigrationTest {

    private static final Path SRC = Path.of("src/main/java");

    @Test
    @DisplayName("cart-service 어디에도 RestClient 가 남아 있지 않다")
    void noRestClientLeft() throws IOException {
        assertThat(filesContaining("import org.springframework.web.client.RestClient")).as("아직 RestClient 를 쓰는 파일").isEmpty();
    }

    @Test
    @DisplayName("@FeignClient(name = \"product-service\") 인터페이스가 있다")
    void hasProductFeignClient() throws IOException {
        assertThat(filesContaining("@FeignClient(name = \"product-service\")")).as("product-service 용 FeignClient").isNotEmpty();
    }

    @Test
    @DisplayName("@EnableFeignClients 가 켜져 있다")
    void feignEnabled() throws IOException {
        assertThat(filesContaining("@EnableFeignClients")).isNotEmpty();
    }

    @Test
    @DisplayName("서킷 브레이커 호출(circuitBreakerFactory.create(\"product\"))은 그대로 남아 있다")
    void circuitBreakerKept() throws IOException {
        assertThat(filesContaining("circuitBreakerFactory.create(\"product\")")).isNotEmpty();
    }

    private static List<Path> filesContaining(String text) throws IOException {
        try (Stream<Path> files = Files.walk(SRC)) {
            return files.filter(p -> p.toString().endsWith(".java"))
                    .filter(p -> {
                        try {
                            return Files.readString(p).contains(text);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
        }
    }
}
