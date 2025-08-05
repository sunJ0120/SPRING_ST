package jpabook.jpa2.exception;

/*
stock 부족 예외 클래스
- RuntimeException을 상속받아, unchecked exception으로 처리한다.
- 생성자 오버로딩을 통해 다양한 방식으로 예외를 생성할 수 있다.
- cause, message를 전달하는 생성자들로 구성되어 있다.
    - 이를 통해, 유연한 예외 처리가 가능하다.
 */
public class NotEnoughStockException extends RuntimeException {
    public NotEnoughStockException() {

    }
    public NotEnoughStockException(String message) {
        super(message);
    }
    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }
}
