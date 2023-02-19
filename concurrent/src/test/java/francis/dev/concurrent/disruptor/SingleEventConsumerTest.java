package francis.dev.concurrent.disruptor;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import francis.dev.concurrent.disruptor.SingleEventConsumer.ValueEvent;
import java.util.concurrent.ThreadFactory;
import org.junit.jupiter.api.Test;

class SingleEventConsumerTest {

  ThreadFactory threadFactory = DaemonThreadFactory.INSTANCE;
  WaitStrategy waitStrategy = new BusySpinWaitStrategy();
  Disruptor<ValueEvent> disruptor = new Disruptor<>(ValueEvent.EVENT_FACTORY, 16,
      threadFactory, ProducerType.SINGLE, waitStrategy);

  @Test
  public void testDisruptor() {
    disruptor.handleEventsWith(new SingleEventConsumer().getEventHandler());
    RingBuffer<ValueEvent> ringBuffer = disruptor.start();
    for (int eventCount = 0; eventCount < 32; eventCount++) {
      long sequenceId = ringBuffer.next();
      ValueEvent valueEvent = ringBuffer.get(sequenceId);
      valueEvent.setValue(eventCount);
      ringBuffer.publish(sequenceId);
    }
  }


}