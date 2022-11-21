package pe.consumer.groups.order.config;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import org.apache.kafka.clients.consumer.ConsumerConfig;

@Slf4j
@Configuration
public class OrderConsumerConfig {

  @Value(value = "${kafka.bootstrapAddress}")
  private String bootstrapAddress;

  @Value(value = "${purchase.ticket.group.name}")
  private String purchaseGroupId;

  @Bean
  public ConsumerFactory<String, JSONObject> testConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, purchaseGroupId);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 3072000);   //메시지 사이즈 변경 (default:1MB -> 3MB)

    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(JSONObject.class, false));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, JSONObject> testListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, JSONObject> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConcurrency(1); /// consumer 를 처리하는 Thread 개수로 Partition에 할당 됨.
    factory.setConsumerFactory(testConsumerFactory());
    factory.getContainerProperties().setAckMode(AckMode.MANUAL);
    factory.getContainerProperties().setPollTimeout(5000);
    factory.setErrorHandler(((exception, data) -> {
      log.error("■■■■■■■■ listener Exception {} and the record is {}", exception, data);   //topic null or 없을 경우 예외 처리 추가.
    }));

    return factory;
  }
}
