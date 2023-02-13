package francis.dev.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import org.junit.jupiter.api.Test;

public class ThreadTest {

  @Test
  public void testYield() throws InterruptedException {
    Thread thread1 = new Thread(() -> System.out.println("thread1"));
    Thread thread2 = new Thread(() -> System.out.println("thread2"));
    Thread thread3 = new Thread(() -> System.out.println("thread3"));

    thread1.start();
    thread1.join();
    thread2.start();
    thread2.join();
    thread3.start();
    thread3.join();
  }

  @Test
  public void testCallable() throws ExecutionException, InterruptedException {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Future<String> future = executorService.submit(() -> "callable");
    assertEquals(future.get(), "callable");
    executorService.shutdown();
  }

  @Test
  public void testFutureTask() throws ExecutionException, InterruptedException {
    FutureTask<String> futureTask = new FutureTask<>(() -> "futureTask");
    new Thread(futureTask).start();
    assertEquals(futureTask.get(), "futureTask");
  }

  @Test
  public void testThreadLocal() throws InterruptedException {
    ThreadLocal<DateFormat> threadLocal = ThreadLocal.withInitial(
        () -> new SimpleDateFormat("yyyy-MM-dd"));
    int executeCount = 100;
    int threadCount = 20;
    Semaphore semaphore = new Semaphore(threadCount);
    CountDownLatch countDownLatch = new CountDownLatch(executeCount);
    ExecutorService executorService = Executors.newCachedThreadPool();
    for (int i = 0; i < executeCount; i++) {
      executorService.execute(() -> {
        try {
          semaphore.acquire();
          try {
            threadLocal.get().parse("2022-01-01");
          } catch (ParseException e) {
            System.out.println("thread:" + Thread.currentThread().getName() + "format fail");
            System.exit(1);
          }
          semaphore.release();
        } catch (InterruptedException e) {
          System.out.println("semaphore error");
        }
        countDownLatch.countDown();
      });
    }
    countDownLatch.await();
    executorService.shutdown();
  }


}
