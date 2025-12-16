package this_is_java.multi_thread;

public class ThreadB extends Thread {
  private WorkObject workObject;

  public ThreadB(WorkObject workObject) {
    setName("ThreadB");
    this.workObject = workObject;
  }

  public void run(){
    for (int i = 1; i <= 10; i++) {
      workObject.methodB();
    }
  }
}
