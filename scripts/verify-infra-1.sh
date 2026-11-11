#!/usr/bin/env bash
# [과제 INFRA-1 검증] 10개 서비스가 Dockerfile 과 compose 에 모두 있는지, 실제로 떠서 Gateway 로 응답하는지 확인합니다.
#   사용법: ./scripts/verify-infra-1.sh          (파일 검사만)
#           ./scripts/verify-infra-1.sh --run    (빌드 → 기동 → Gateway 호출까지)
set -u
SERVICES="config-server discovery-server gateway-server order-service product-service payment-service user-service seller-service cart-service settlement-service"
fail=0

echo "[1] Dockerfile 검사"
for s in $SERVICES; do
  if [ -f "$s/Dockerfile" ]; then echo "  ✅ $s/Dockerfile"; else echo "  ❌ $s/Dockerfile 없음"; fail=1; fi
done

echo "[2] docker-compose.yml 서비스 검사"
declared=$(docker compose config --services 2>/dev/null)
for s in $SERVICES; do
  if echo "$declared" | grep -qx "$s"; then echo "  ✅ $s"; else echo "  ❌ compose 에 $s 없음"; fail=1; fi
done

if [ "${1:-}" = "--run" ] && [ $fail -eq 0 ]; then
  echo "[3] 빌드 · 기동 · 호출"
  ./gradlew bootJar -q && docker compose up -d --build || exit 1
  echo "  서비스가 Eureka 에 등록될 때까지 최대 3분 기다립니다..."
  for i in $(seq 1 36); do
    if curl -sf http://localhost:8000/products/1 >/dev/null; then echo "  ✅ Gateway → product-service 응답 OK"; break; fi
    sleep 5
    [ "$i" = 36 ] && { echo "  ❌ Gateway 응답 없음 (docker compose logs gateway-server 확인)"; fail=1; }
  done
fi

[ $fail -eq 0 ] && echo "🎉 통과" || { echo "아직 남은 항목이 있습니다."; exit 1; }
