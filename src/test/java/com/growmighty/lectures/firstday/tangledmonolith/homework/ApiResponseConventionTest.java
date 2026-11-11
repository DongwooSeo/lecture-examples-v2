package com.growmighty.lectures.firstday.tangledmonolith.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * [과제 DDD-3 검증 테스트] 모든 API 가 공통 응답 ApiResponse 로 감싸져 있고, 전역 예외 처리기가 있는지 검사합니다.
 *
 * 공통 응답 클래스 위치: com.growmighty.lectures.firstday.tangledmonolith.common.response.ApiResponse
 * 예상 모양: { "success": true, "data": {...} } / { "success": false, "error": { "code", "message" } }
 */
class ApiResponseConventionTest {

    private static final String ROOT = "com.growmighty.lectures.firstday.tangledmonolith";
    private static final String API_RESPONSE = ROOT + ".common.response.ApiResponse";

    @Test
    @DisplayName("common.response.ApiResponse 클래스가 있어야 한다")
    void apiResponseExists() {
        assertThat(loadOrNull(API_RESPONSE)).as(API_RESPONSE + " 를 만들어 주세요").isNotNull();
    }

    @Test
    @DisplayName("모든 컨트롤러 메서드는 ApiResponse(또는 ResponseEntity<ApiResponse>)를 반환한다")
    void everyEndpointReturnsApiResponse() throws Exception {
        List<String> violations = new ArrayList<>();
        for (Class<?> controller : scan(RestController.class)) {
            for (Method m : controller.getDeclaredMethods()) {
                if (!AnnotatedElementUtils.hasAnnotation(m, RequestMapping.class)) {
                    continue;
                }
                if (!wrapsApiResponse(m.getGenericReturnType())) {
                    violations.add(controller.getSimpleName() + "#" + m.getName() + " → " + m.getGenericReturnType().getTypeName());
                }
            }
        }
        assertThat(violations).as("ApiResponse 로 감싸지 않은 API").isEmpty();
    }

    @Test
    @DisplayName("@RestControllerAdvice 전역 예외 처리기가 있어야 한다")
    void globalExceptionHandlerExists() throws Exception {
        assertThat(scan(RestControllerAdvice.class)).as("GlobalExceptionHandler(@RestControllerAdvice)를 만들어 주세요").isNotEmpty();
    }

    private static boolean wrapsApiResponse(Type type) {
        if (type instanceof ParameterizedType pt) {
            String raw = pt.getRawType().getTypeName();
            if (raw.equals(API_RESPONSE)) {
                return true;
            }
            if (raw.equals("org.springframework.http.ResponseEntity")) {
                return wrapsApiResponse(pt.getActualTypeArguments()[0]);
            }
            return false;
        }
        return type.getTypeName().equals(API_RESPONSE);
    }

    private static List<Class<?>> scan(Class<? extends java.lang.annotation.Annotation> annotation) throws ClassNotFoundException {
        var scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(annotation));
        List<Class<?>> result = new ArrayList<>();
        for (BeanDefinition bd : scanner.findCandidateComponents(ROOT)) {
            result.add(Class.forName(bd.getBeanClassName()));
        }
        return result;
    }

    private static Class<?> loadOrNull(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
