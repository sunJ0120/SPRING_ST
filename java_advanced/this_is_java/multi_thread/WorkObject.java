package this_is_java.multi_thread;

public class WorkObject {
  public synchronized void methodA(){
    Thread t1 = Thread.currentThread();
    System.out.println(t1.getName() + ": methodA 작업 실행");
    notify();    // 다른 스레드를 실행 대기 상태로
    try{
      wait();    // 자신의 스레드를 일시정지
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public synchronized void methodB(){
    Thread t2 = Thread.currentThread();
    System.out.println(t2.getName() + ": methodB 작업 실행");
    notify();    // 다른 스레드를 실행 대기 상태로
    try{
      wait();    // 자신의 스레드를 일시정지
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
