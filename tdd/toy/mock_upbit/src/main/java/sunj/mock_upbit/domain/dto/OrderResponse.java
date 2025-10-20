package sunj.mock_upbit.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 클래스 이름: OrderResponse
 * <p>
 * 버전 정보: 1.0
 * <p>
 * 날짜: 2025-10-20
 */
@Getter
@Builder
@AllArgsConstructor
public class OrderResponse {
    private String market;
    private String uuid;
    private String price;
    private State state;

    /**
     * 업비트 API 응답을 DTO로 반환할 정적 팩토리 메서드
     */
    public static OrderResponse from(UpbitOrderResponse upbitResponse){
        return OrderResponse.builder()
                .market(upbitResponse.market)
                .uuid(upbitResponse.uuid)
                .price(upbitResponse.price)
                .state(upbitResponse.state)
                .build();
    }
}