package francis.dev.unittest;


import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class RedisCacheTest {

	private RedisCache cache;

	@Container
	public GenericContainer redis = new GenericContainer(DockerImageName.parse("redis:5.0.3-alpine"))
			.withExposedPorts(6379);

	@BeforeEach
	public void setUp() {
		String address = redis.getHost();
		Integer port = redis.getFirstMappedPort();
		RedisClient redisClient = RedisClient.create(RedisURI.create(address, port));
		cache = new RedisCache(redisClient);
	}

	@Test
	public void testSimplePutAndGet() {
		cache.put("test", "example");
		Optional<String> foundObject = cache.get("test", String.class);

		Assertions.assertTrue(foundObject.isPresent());
		Assertions.assertEquals("example", foundObject.get());
	}
}