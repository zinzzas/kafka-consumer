package pe.consumer.groups.order.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import javax.annotation.PostConstruct;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {
  private final MongoTemplate mongoTemplate;
  private MongoCollection<Document> mongoCollection;

  public OrderRepository(@Qualifier("defaultMongoTemplate") MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @PostConstruct
  public void init() {
    mongoCollection = mongoTemplate.getCollection("order_purchase");
  }

  public InsertOneResult insertOrderPurchases(Document document) {
    return mongoCollection.insertOne(document);
  }
}
