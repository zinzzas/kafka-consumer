package pe.consumer.groups.order;

import com.mongodb.client.result.InsertOneResult;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.bson.Document;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import pe.consumer.groups.order.repository.OrderRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderKafkaConsumer {

  private final OrderRepository orderRepository;

  @KafkaListener(topics = "${purchase.ticket.topic.name}",
      groupId = "${purchase.ticket.group.name}",
      containerFactory = "testListenerContainerFactory")
  public void consumer(JSONObject jsonObject, Acknowledgment ack) {

    log.info("[consumer rev] 메세지 수신완료 -> {}" + jsonObject);

    Document doc = Document.parse(jsonObject.toString());
    InsertOneResult result = orderRepository.insertOrderPurchases(doc);

    if (Objects.nonNull(result.getInsertedId())) {
      ack.acknowledge();
    }
  }
}
