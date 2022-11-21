package pe.consumer.support.config;

import com.mongodb.client.MongoClient;
import javax.annotation.Resource;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@ConfigurationProperties(prefix = "mongodb.order")
public class DefaultMongoConfig implements MongoConfig {

  @Resource
  @Qualifier("defaultMongoClient")
  private MongoClient defaultMongoClient;

  @Setter
  private String dbname;

  @Primary
  @Bean("defaultMongoTemplate")
  public MongoTemplate mongoTemplate() {
    return new MongoTemplate(defaultMongoClient, dbname);
  }
}
