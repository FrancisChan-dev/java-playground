package francis.dev.concurrent;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class ThreadLocalTest {

  private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

  @Test
  public void testThreadLocal() {
    Thread threadA = new Thread(() -> {
      threadLocal.set("ThreadA:" + Thread.currentThread().getName());
      System.out.println("线程A本地变量中的值为:" + threadLocal.get());
      threadLocal.remove();
      System.out.println("线程A删除本地变量后ThreadLocal中的值为:" + threadLocal.get());
      assertThat(threadLocal.get()).isNull();
    });
    Thread threadB = new Thread(() -> {
      threadLocal.set("ThreadB:" + Thread.currentThread().getName());
      System.out.println("线程B本地变量中的值为:" + threadLocal.get());
      assertThat(threadLocal.get()).isEqualTo(Thread.currentThread().getName());
    });
    threadA.start();
    threadB.start();
  }
}