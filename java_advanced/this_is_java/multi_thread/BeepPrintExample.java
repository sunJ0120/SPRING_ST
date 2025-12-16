package this_is_java.multi_thread;

import java.awt.Toolkit;

public class BeepPrintExample {

  public static void main(String[] args) {

    Thread t1 = new Thread(new Runnable() {
      public void run() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        for (int i = 0; i < 5; i++) {
          toolkit.beep();
          try {
            Thread.sleep(500);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    });

    t1.start();

    for (int i = 0; i < 5; i++) {
      System.out.println("띵");
      try{
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
