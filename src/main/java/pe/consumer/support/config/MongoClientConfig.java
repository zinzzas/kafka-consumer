package pe.consumer.support.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoClientConfig implements MongoConfig {

  @Value("${mongodb.uri}")
  private String uri;

  @Bean("defaultMongoClient")
  public MongoClient defaultMongoClient() {
    return MongoClients.create(mongoClientSettings(uri, defaultPoolSettings()));
  }
}
