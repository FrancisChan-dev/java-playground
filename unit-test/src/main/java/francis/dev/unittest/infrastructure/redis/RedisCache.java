package francis.dev.unittest.infrastructure.redis;


import com.google.gson.Gson;
import francis.dev.unittest.infrastructure.Cache;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import java.util.Optional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RedisCache implements Cache {

  private final Gson gson;
  private StatefulRedisConnection<String, String> connect;

  public RedisCache(RedisClient redisClient) {
    this.connect = redisClient.connect();
    this.gson = new Gson();
  }

  @Override
  public void put(String key, Object value) {
    String jsonValue = gson.toJson(value);
    this.connect.sync().set(key, jsonValue);
  }

  @Override
  public <T> Optional<T> get(String key, Class<T> expectedClass) {
    String foundJson = this.connect.sync().get(key);
    if (foundJson == null) {
      return Optional.empty();
    }
    return Optional.of(gson.fromJson(foundJson, expectedClass));
  }
}
