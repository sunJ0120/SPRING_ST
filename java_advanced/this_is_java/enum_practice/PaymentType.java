package this_is_java.enum_practice;

public enum PaymentType {
    CREDIT_CARD{
        @Override
        public void pay(int amount){
            System.out.println("카드로 " + amount + "원 결제");
        }
    },
    BANK_CARD{
        @Override
        public void pay(int amount){
            System.out.println("계좌이체로 " + amount + "원 결제");
        }
    };
    public abstract void pay(int amount);
}
