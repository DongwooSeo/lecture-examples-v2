# user-service 이미지 — "jar 를 컨테이너에 담아 배송한다"
# 1) 먼저 jar 를 만든다:   ./gradlew :user-service:bootJar
# 2) 이미지를 굽는다:      docker build -t user-service:local user-service
FROM eclipse-temurin:21-jre

WORKDIR /app

# bootJar 결과물만 복사 (-plain.jar 는 의존성이 빠진 jar 라 실행 불가)
COPY build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8083

# 컨테이너 메모리 한도에 맞춰 힙을 잡는다 (고정 -Xmx 대신 비율)
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/app/app.jar"]
