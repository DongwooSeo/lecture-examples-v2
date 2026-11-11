#!/usr/bin/env bash
# [과제 INFRA-2 검증] 모든 서비스에 CI 워크플로가 있고, 공통 로직이 재사용 워크플로 하나로 모였는지 확인합니다.
set -u
SERVICES="config-server discovery-server gateway-server order-service product-service payment-service user-service seller-service cart-service settlement-service"
fail=0
reusable=$(grep -l "workflow_call" .github/workflows/*.yml 2>/dev/null | head -1)
if [ -n "$reusable" ]; then echo "✅ 재사용 워크플로: $reusable"; else echo "❌ on: workflow_call 을 쓰는 재사용 워크플로가 없습니다"; fail=1; fi
for s in $SERVICES; do
  f=".github/workflows/$s.yml"
  if [ ! -f "$f" ]; then echo "❌ $f 없음"; fail=1; continue; fi
  if ! grep -q "\"$s/\*\*\"" "$f"; then echo "❌ $f — paths 필터에 \"$s/**\" 가 없습니다 (다른 서비스가 바뀔 때도 돌게 됨)"; fail=1; continue; fi
  if [ -n "$reusable" ] && ! grep -q "uses: ./$reusable" "$f"; then echo "❌ $f — 재사용 워크플로를 호출하지 않습니다"; fail=1; continue; fi
  echo "✅ $f"
done
[ $fail -eq 0 ] && echo "🎉 통과 — 이제 push 해서 Actions 탭에서 녹색 체크를 확인하세요" || exit 1
