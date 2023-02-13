package francis.dev.unittest;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import francis.dev.unittest.models.OrderCommission;
import java.math.BigDecimal;
import org.junit.Test;

public class ExceptionTest {

  @Test
  public void testBuilderNullPointException() {
    assertThatNullPointerException().isThrownBy(() -> OrderCommission.builder().build());
  }

  @Test(expected = NullPointerException.class)
  public void testBuilderNullPointException2() {
    OrderCommission.builder()
        .orderId(123L)
        .userId(123L)
        .build();
  }

  @Test()
  public void testBuilderSuccess() {
    assertDoesNotThrow(() -> OrderCommission.builder()
        .orderId(123L)
        .userId(123L)
        .amount(BigDecimal.ZERO)
        .build());
  }

}
