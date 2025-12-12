package this_is_java;

public class BitLogicExample {
    public static void main(String[] args) {
        System.out.println("45 & 25 = " + (45 & 25));    // 각각을 4byte의 정수로 바꿔서 and (같아야 1) 계산
        System.out.println("45 | 25 = " + (45 | 25));    // 각각을 4byte의 정수로 바꿔서 or (하나만 1) 계산
        System.out.println("45 ^ 25 = " + (45 ^ 25));    // 각각을 4byte의 정수로 바꿔서 xor (달라야 1) 계산
        System.out.println("~45 = " + (~45));    // 4byte의 정수로 바꿔서 부정 연산 계산

        /**
         * byte code를 unsigned 정수로 얻기 위해서는,
         * 맨 앞자리의 1(음수 표식)을 없애기 위해
         * 255 (4byte, int 최대)와 & 연산을 해서 전 자릿수를 양수로 살려야 한다.
         */
        byte receiveData = -120;

        int unsignedInt1 =  receiveData & 255;
        int unsignedInt2 = Byte.toUnsignedInt(receiveData);

        System.out.println(unsignedInt1 == unsignedInt2);

        /**
         * 그래서 실제로 136 (int라 4byte)을 byte화 하면
         * 120이라는 전혀 다른 수가 나온다.
         */
        int test = 136;
        byte btest = (byte) test;

        System.out.println(btest == test);
        System.out.println(btest);
    }
}