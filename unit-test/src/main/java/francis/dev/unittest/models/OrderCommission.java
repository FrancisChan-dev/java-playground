package francis.dev.unittest.models;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
@Builder
public class OrderCommission {

  @NonNull
  private Long orderId;

  @NonNull
  private Long userId;

  @NonNull
  private BigDecimal amount;

}
