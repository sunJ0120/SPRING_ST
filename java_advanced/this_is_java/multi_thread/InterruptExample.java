package this_is_java.multi_thread;

public class InterruptExample {

  public static void main(String[] args) {
    Thread thread = new PrintThread();
    thread.start();

    try{
      Thread.sleep(1000);
    } catch (InterruptedException e) {

    }

    thread.interrupt();    // 스레드를 정리하라는 신호를 보낸다.
  }
}
