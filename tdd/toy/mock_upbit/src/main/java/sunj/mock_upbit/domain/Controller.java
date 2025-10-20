package sunj.mock_upbit.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sunj.mock_upbit.domain.dto.OrderRequest;
import sunj.mock_upbit.domain.dto.OrderResponse;

/**
 * 클래스 이름: Controller
 * <p>
 * 버전 정보: 1.0
 * <p>
 * 날짜: 2025-10-20
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {
    private final TradingService tradingService;
    // TODO : 유저가 특정 상품 특정 갯수 거래 신청 요청을 한다.
    @PostMapping("/order")
    public ResponseEntity<OrderResponse> buyMarketOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse response = tradingService.buy(orderRequest.getMarket(), orderRequest.getPrice());
        return ResponseEntity.ok(response);
    }
}
