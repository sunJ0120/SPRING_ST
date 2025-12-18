package this_is_java.multi_thread;

public class PrintThread extends Thread {
  public void run(){
    try{
      while(true){
        System.out.println("실행 중");
        Thread.sleep(1);    // sleep 상태인데 interrupt로 InterruptedException를 만나서 자원 정리
      }
    } catch (InterruptedException e) {
    }
    System.out.println("리소스 정리");
    System.out.println("실행 종료");
  }
}
