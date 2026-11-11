package com.growmighty.lectures.firstday.tangledmonolith.homework;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * [과제 DDD-1 검증 테스트] 도메인 경계를 넘는 JPA 연관관계가 0개인지 검사합니다.
 *
 * 같은 도메인 안의 연관관계(예: Order ↔ OrderItem)는 허용하고,
 * 다른 도메인 Entity를 @OneToOne / @ManyToOne / @OneToMany / @ManyToMany 로 들고 있으면 실패합니다.
 * 실패 메시지에 "어느 Entity의 어느 필드"가 경계를 넘는지 나옵니다.
 */
class DomainBoundaryTest {

    private static final String ROOT = "com.growmighty.lectures.firstday.tangledmonolith";

    @Test
    @DisplayName("다른 도메인 Entity를 객체로 직접 참조하는 연관관계가 없어야 한다")
    void noCrossDomainAssociations() throws Exception {
        List<String> violations = new ArrayList<>();

        for (Class<?> entity : findEntities()) {
            String ownDomain = domainOf(entity);
            for (Field field : entity.getDeclaredFields()) {
                if (!isAssociation(field)) {
                    continue;
                }
                Class<?> target = targetType(field);
                String targetDomain = domainOf(target);
                if (!ownDomain.equals(targetDomain)) {
                    violations.add(String.format("%s.%s → %s (%s 도메인 → %s 도메인)",
                            entity.getSimpleName(), field.getName(), target.getSimpleName(), ownDomain, targetDomain));
                }
            }
        }

        assertThat(violations)
                .as("ID 간접 참조로 바꿔야 할 연관관계")
                .isEmpty();
    }

    private static List<Class<?>> findEntities() throws ClassNotFoundException {
        var scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        List<Class<?>> result = new ArrayList<>();
        for (BeanDefinition bd : scanner.findCandidateComponents(ROOT)) {
            result.add(Class.forName(bd.getBeanClassName()));
        }
        assertThat(result).as("Entity 를 하나도 찾지 못했습니다").isNotEmpty();
        return result;
    }

    private static boolean isAssociation(Field f) {
        return f.isAnnotationPresent(OneToOne.class) || f.isAnnotationPresent(ManyToOne.class)
                || f.isAnnotationPresent(OneToMany.class) || f.isAnnotationPresent(ManyToMany.class);
    }

    private static Class<?> targetType(Field f) {
        Type generic = f.getGenericType();
        if (generic instanceof ParameterizedType pt && pt.getActualTypeArguments().length > 0
                && pt.getActualTypeArguments()[0] instanceof Class<?> c) {
            return c;
        }
        return f.getType();
    }

    /** com.growmighty...tangledmonolith.{domain}.xxx → {domain} */
    private static String domainOf(Class<?> type) {
        String rest = type.getPackageName().substring(ROOT.length()).replaceFirst("^\\.", "");
        int dot = rest.indexOf('.');
        return dot < 0 ? rest : rest.substring(0, dot);
    }
}
