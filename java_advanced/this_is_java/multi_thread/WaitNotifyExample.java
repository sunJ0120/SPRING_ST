package this_is_java.multi_thread;

public class WaitNotifyExample {
  public static void main(String[] args) {
    WorkObject workObject = new WorkObject();

    ThreadA threadA = new ThreadA(workObject);
    ThreadB threadB = new ThreadB(workObject);

    threadA.start();
    threadB.start();
  }
}
