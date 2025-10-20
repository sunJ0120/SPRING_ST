# 업비트 Mock 테스트 실습 프로젝트

## 📚 프로젝트 목적
- **Mock 테스트 학습**: Mockito를 활용한 단위 테스트 작성 연습
- **RDD(Responsibility-Driven Design) 실습**: 각 클래스의 책임을 명확히 분리
- **REST API 통신 이해**: Spring RestTemplate을 사용한 외부 API 호출

## 🎯 왜 Mock을 사용하나?
실제 업비트 API를 호출하면:
- 💰 거래 수수료 발생 (매수/매도 시 0.05%)
- 🐌 네트워크 지연 시간
- 🔑 API 키 관리 필요
- 🎲 외부 서버 의존성

**Mock을 사용하면:**
- ✅ 실제 돈 안 씀
- ✅ 빠른 테스트 실행
- ✅ 원하는 시나리오 자유롭게 재현 (잔액 부족, API 에러 등)
- ✅ API 키 없이도 개발 가능

## 🏗️ 프로젝트 구조
```
Controller (HTTP 요청/응답)
    ↓
TradingService (거래 로직 조율)
    ↓
    ├─→ UpbitClient (업비트 API 통신)
    └─→ BalanceValidator (잔액 검증)
```

### 각 계층의 책임
- **Controller**: API 엔드포인트 제공, DTO 변환
- **TradingService**: 비즈니스 로직 조율 (조회 → 검증 → 거래)
- **UpbitClient**: 업비트 REST API 호출만 담당
- **BalanceValidator**: 잔액 검증 및 예외 처리

## 🚀 주요 기능

### 1. 시장가 매수 (Market Order Buy)
- KRW 금액으로 비트코인 매수
- 현재 시장가로 즉시 체결

### 2. 잔액 검증
- 매수 전 사용자 잔액 확인
- 잔액 부족 시 예외 발생

### 참고
[업비트 주문 생성 API 공식 문서](https://docs.upbit.com/kr/reference/new-order)