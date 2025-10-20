package sunj.mock_upbit.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sunj.mock_upbit.domain.dto.OrderResponse;

/**
 * 클래스 이름: TradingService
 * <p>
 * 버전 정보: 1.0
 * <p>
 * 날짜: 2025-10-20
 */
@Service
@RequiredArgsConstructor
public class TradingService {
    private final BalanceValidator balanceValidator;    // Calculator는 Validator가 직접 쓰므로, 여기서 정의한다.
    private final UpbitClient upbitClient;

    public OrderResponse buy(String market, String price){
        // TODO : upbitClient로 잔액 조회
        // TODO : balanceValidator로 잔액과 주문 금액 예외 확인
        // TODO : upbitClient로 주문
        // TODO : 주문 결과 Response로 담아서 반환
    }
}
