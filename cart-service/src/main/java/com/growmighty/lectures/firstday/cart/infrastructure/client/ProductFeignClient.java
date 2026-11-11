package com.growmighty.lectures.firstday.cart.infrastructure.client;

import com.growmighty.lectures.firstday.cart.infrastructure.client.dto.ApiResponseBody;
import com.growmighty.lectures.firstday.cart.infrastructure.client.dto.ProductApiData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// [과제 MSA-2 정답] cart → product 호출을 RestClient 손코딩에서 선언적 Feign 으로.
// name = Eureka 에 등록된 상대 서비스 이름. URL 이 아니다.
@FeignClient(name = "product-service")
public interface ProductFeignClient {

    @GetMapping("/products/{productId}")
    ApiResponseBody<ProductApiData> fetchProduct(@PathVariable("productId") Long productId);
}
