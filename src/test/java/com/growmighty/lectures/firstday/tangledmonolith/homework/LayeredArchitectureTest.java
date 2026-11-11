package com.growmighty.lectures.firstday.tangledmonolith.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * [과제 DDD-3 검증 테스트] 6개 도메인이 DDD 4계층으로 나뉘었는지, domain 계층이 순수한지 소스 코드를 읽어 검사합니다.
 * (ArchUnit 없이 import 문만 봅니다. ./gradlew test 를 프로젝트 루트에서 실행하세요.)
 */
class LayeredArchitectureTest {

    private static final Path ROOT = Path.of("src/main/java/com/growmighty/lectures/firstday/tangledmonolith");
    private static final String BASE = "com.growmighty.lectures.firstday.tangledmonolith.";
    private static final List<String> DOMAINS = List.of("cart", "order", "payment", "product", "seller", "user");
    private static final List<String> LAYERS = List.of("presentation", "application", "domain", "infrastructure");

    @Test
    @DisplayName("모든 도메인에 presentation / application / domain / infrastructure 패키지가 있어야 한다")
    void everyDomainHasFourLayers() {
        List<String> missing = new ArrayList<>();
        for (String domain : DOMAINS) {
            for (String layer : LAYERS) {
                if (!Files.isDirectory(ROOT.resolve(domain).resolve(layer))) {
                    missing.add(domain + "/" + layer);
                }
            }
        }
        assertThat(missing).as("아직 만들지 않은 계층 패키지").isEmpty();
    }

    @Test
    @DisplayName("도메인 루트에 계층 밖으로 남은 클래스가 없어야 한다")
    void noClassLeftOutsideLayers() throws IOException {
        List<String> leftovers = new ArrayList<>();
        for (String domain : DOMAINS) {
            Path dir = ROOT.resolve(domain);
            if (!Files.isDirectory(dir)) {
                continue;
            }
            try (Stream<Path> files = Files.list(dir)) {
                files.filter(p -> p.toString().endsWith(".java"))
                        .forEach(p -> leftovers.add(domain + "/" + p.getFileName()));
            }
        }
        assertThat(leftovers).as("계층 패키지로 옮겨야 할 클래스").isEmpty();
    }

    @Test
    @DisplayName("domain 계층은 스프링·웹과 다른 계층을 import 하지 않는다 (DIP)")
    void domainLayerIsPure() throws IOException {
        List<String> violations = new ArrayList<>();
        for (String domain : DOMAINS) {
            Path dir = ROOT.resolve(domain).resolve("domain");
            if (!Files.isDirectory(dir)) {
                continue;
            }
            try (Stream<Path> files = Files.walk(dir)) {
                for (Path file : files.filter(p -> p.toString().endsWith(".java")).toList()) {
                    for (String line : Files.readAllLines(file)) {
                        String l = line.trim();
                        if (!l.startsWith("import ")) {
                            continue;
                        }
                        boolean forbidden = l.startsWith("import org.springframework.")
                                || l.startsWith("import jakarta.servlet.")
                                || (l.startsWith("import " + BASE)
                                && (l.contains(".application.") || l.contains(".infrastructure.") || l.contains(".presentation.")));
                        if (forbidden) {
                            violations.add(domain + "/domain/" + file.getFileName() + " → " + l);
                        }
                    }
                }
            }
        }
        assertThat(violations).as("domain 계층이 의존하면 안 되는 import").isEmpty();
    }
}
