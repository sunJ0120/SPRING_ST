package this_is_java.multi_thread;

public class SumThread extends Thread{
  private long sum = 0;

  public long getSum() {
    return sum;
  }

  public void setSum(long sum) {
    this.sum = sum;
  }

  public void run() {
    for (long i = 1; i <= 10; i++) {
      sum += i;
    }
  }
}
