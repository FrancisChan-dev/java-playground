package francis.dev.unittest.infrastructure.kafka;

import java.util.concurrent.CountDownLatch;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class KafkaConsumer {

  private CountDownLatch latch = new CountDownLatch(1);
  private String payload;

  @KafkaListener(topics = "${test.topic}")
  public void receive(ConsumerRecord<?, ?> consumerRecord) {
    log.info("received data '{}'", consumerRecord.toString());
    payload = consumerRecord.toString();
    latch.countDown();
  }

  public void resetLatch() {
    latch = new CountDownLatch(1);
  }
}
