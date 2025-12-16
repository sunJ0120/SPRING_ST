package this_is_java.multi_thread;

public class ThreadA extends Thread {
  private WorkObject workObject;

  public ThreadA(WorkObject workObject) {
    setName("ThreadA");
    this.workObject = workObject;
  }

  public void run(){
    for (int i = 1; i <= 10; i++) {
      workObject.methodA();
    }
  }
}
