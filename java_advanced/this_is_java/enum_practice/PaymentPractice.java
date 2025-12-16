package this_is_java.enum_practice;

public class PaymentPractice {
    public static void main(String[] args) {
        PaymentType paymentType = PaymentType.CREDIT_CARD;

        // if-else 없이 원하는 대로 조립 가능
        paymentType.pay(10000);
    }
}
