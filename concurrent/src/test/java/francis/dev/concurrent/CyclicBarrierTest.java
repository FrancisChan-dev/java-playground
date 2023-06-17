package francis.dev.concurrent;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import org.junit.Test;

/**
 * 山上住了一位武林大师（master）,擅长一种失传绝学“还原靓靓拳（beautifyHit）”。每天都有成千上万的男孩（boy）慕名上山求打脸。山路很窄,只能容一个人的站位。男孩们每天排队上山，大师则按顺序给他们运功。因为大师轻功了得，所以大师运功期间不需要站在山路上。被打的男孩，如果是方（square）脸就会变成圆（round）脸，如果是圆脸就会变成尖（peaky）脸，如果是尖脸就会变成方脸。
 * 1. 如果山路实在太窄了，被打的男孩每天都只能按顺序的从原路离开山路。那山路属于哪种数据结构？ 2.
 * 如果哪天大师突然对靓仔起了怜悯之心，决定先从年纪小的开始打。而山路又很窄，每次只能让相邻的两个男孩交换位置。请你为大师想一个方法，让他可以实现他的靓仔梦。 3.
 * 请你用任何你熟悉的程序语言为大师实现一个单例模式（单件、singleton），以彰显大师的寂寞。 4.
 * 请你用任何你熟悉的面向对象的程序语言来描述大师跟男孩们的打脸活动，要求不能用if、switch、select、while、until等关键词，不能用array、dictionary（字典）、map（映射表）等类似结构，不能用?：（三元操作符）、||、&&、or、and，不能用
 * +、-、*、/、%（取余）等运算符。 5. 假设某天，大师习得了“左右互搏大法”，即大师可以左右手同时对不同的男孩施展“还原靓靓拳”。且满足以下规则： 1. 左右手运功所需的时间都是不固定的
 * 2. 每只手运功的过程不能被打断 3. 每只手打完一个男孩就能打下一个 4. 一个男孩同时只能被一只手打 5. 只能是相邻的男孩才能同时被打（受限于大师的臂长）
 */
public class CyclicBarrierTest {

  private Random random = new Random();
  private ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();

  class Hand extends Thread {

    private String title;

    private CyclicBarrier cyclicBarrier;
    private CountDownLatch countDownLatch;

    public Hand(String title, CyclicBarrier cyclicBarrier, CountDownLatch countDownLatch) {
      this.title = title;
      this.cyclicBarrier = cyclicBarrier;
      this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
      try {
        while (!queue.isEmpty()) {
          Integer poll = queue.poll();
          int cost = random.nextInt(20);
          Thread.sleep(cost);
          System.out.println(title + " cost: " + cost + "ms," + " poll: " + poll);
          cyclicBarrier.await();
        }
        countDownLatch.countDown();
        cyclicBarrier.await();
      } catch (InterruptedException | BrokenBarrierException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Test
  public void test() throws InterruptedException {
    offer(9);
    CyclicBarrier barrier = new CyclicBarrier(2,
        () -> System.out.println("left and right work together end"));
    CountDownLatch countDownLatch = new CountDownLatch(2);
    Hand left = new Hand("left hand", barrier, countDownLatch);
    Hand right = new Hand("right hand", barrier, countDownLatch);
    left.start();
    right.start();
    countDownLatch.await();
  }


  public void offer(int i) {
    for (int j = 0; j < i; j++) {
      this.queue.offer(j);
    }
  }


}
