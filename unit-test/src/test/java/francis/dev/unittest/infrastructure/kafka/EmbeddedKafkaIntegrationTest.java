package francis.dev.unittest.infrastructure.kafka;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092",
    "port=9092"})
@ActiveProfiles("kafka")
public class EmbeddedKafkaIntegrationTest {

  @Autowired
  private KafkaConsumer consumer;

  @Autowired
  private KafkaProducer producer;

  @Value("${test.topic}")
  private String topic;

  @Test
  public void givenEmbeddedKafkaBroker_whenSendingWithSimpleProducer_thenMessageReceived()
      throws InterruptedException {
    String data = "sending by KafkaProducer";

    producer.send(topic, data);

    boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
    assertThat(consumer.getPayload(), containsString(data));
    assertThat(messageConsumed, is(true));
  }
}
