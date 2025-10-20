package sunj.mock_upbit.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 클래스 이름: OrderRequest
 * <p>
 * 버전 정보: 1.0
 * <p>
 * 날짜: 2025-10-20
 */
@Getter
@Builder
@AllArgsConstructor
public class OrderRequest {
    private String market;    // 거래 마켓 코드
    private String price;    // 가격 (시장가 매수이므로 주문 총액)
}
