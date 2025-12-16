package this_is_java.multi_thread;

public class JoinExample {

  public static void main(String[] args) {
    SumThread t1 = new SumThread();
    t1.start();
    try {
      t1.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("1~100의 합 : " + t1.getSum());
  }
}
