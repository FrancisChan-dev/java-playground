package francis.dev.reactor;


import static java.lang.Thread.sleep;
import static java.time.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class StreamTest {

  @Test
  public void testBackPressure() {
    List<Integer> elements = new ArrayList<>();
    Flux.just(1, 2, 3, 4)
        .log()
        .map(i -> i * 2)
        .subscribe(new Subscriber<Integer>() {
          private Subscription s;
          int onNextAmount;

          @Override
          public void onSubscribe(Subscription s) {
            this.s = s;
            s.request(2);
          }

          @Override
          public void onNext(Integer integer) {
            elements.add(integer);
            onNextAmount++;
            if (onNextAmount % 2 == 0) {
              s.request(2);
            }
          }

          @Override
          public void onError(Throwable t) {
          }

          @Override
          public void onComplete() {
          }
        });
    assertThat(elements).containsExactly(2, 4, 6, 8);
  }

  @Test
  public void testTwoStreams() {
    List<Integer> elements = new ArrayList<>();
    Flux.just(1, 2, 3, 4)
        .log()
        .map(i -> i * 2)
        .zipWith(Flux.range(0, Integer.MAX_VALUE), Integer::sum)
        .subscribe(elements::add);
    assertThat(elements).containsExactly(2, 5, 8, 11);
  }

  @Test
  public void testHotStream() {
    List<Long> elements = new ArrayList<>();
    ConnectableFlux<Object> publish = Flux.create(fluxSink -> {
      long end = System.currentTimeMillis() + 1100;
      while (System.currentTimeMillis() <= end) {
        fluxSink.next(System.currentTimeMillis());
      }
    }).sample(ofMillis(500)).publish();
    publish.subscribe(e -> elements.add((Long) e));
    publish.subscribe(e -> elements.add((Long) e));
    publish.connect();

    assertThat(elements.size()).isEqualTo(4);
  }

  @Test
  public void testCurrencyStream() throws InterruptedException {
    List<Long> elements = new ArrayList<>();
    ConnectableFlux<Object> publish = Flux.create(fluxSink -> {
      long end = System.currentTimeMillis() + 1000;
      while (System.currentTimeMillis() <= end) {
        fluxSink.next(System.currentTimeMillis());
      }
    }).sample(ofMillis(100)).publish();
    publish.log().subscribeOn(Schedulers.parallel()).subscribe(e -> elements.add((Long) e));
    publish.connect();

    assertThat(elements.size()).isEqualTo(10);
  }
}
