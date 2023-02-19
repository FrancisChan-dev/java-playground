package francis.dev.concurrent.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class SingleEventConsumer {

  public EventHandler<ValueEvent>[] getEventHandler() {
    EventHandler<ValueEvent> eventEventHandler = (event, sequence, endOfBatch) -> print(event.getValue(), sequence);
    return new EventHandler[]{eventEventHandler};
  }

  private void print(int id, long sequenceId) {
    log.info("id: {}, sequenceId: {}", id, sequenceId);
  }

  @Data
  public static class ValueEvent {
    int value;
    public final static EventFactory<ValueEvent> EVENT_FACTORY = ValueEvent::new;
  }

}
