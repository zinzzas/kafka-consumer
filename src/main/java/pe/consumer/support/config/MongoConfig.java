package pe.consumer.support.config;

import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.connection.ConnectionPoolSettings.Builder;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Configuration;

@Configuration
public interface MongoConfig {

  default MongoClientSettings mongoClientSettings(String uri, Block<Builder> poolSettings) {

    ConnectionString connectionString = new ConnectionString(uri);

    return MongoClientSettings.builder()
                              //.credential(MongoCredential.createCredential(user, database, password.toCharArray()))
                              .retryReads(false)
                              .retryWrites(false)
                              .writeConcern(WriteConcern.ACKNOWLEDGED)
                              .readPreference(ReadPreference.secondaryPreferred())
                              .applyConnectionString(connectionString)
                              .applyToConnectionPoolSettings(poolSettings)
                              .applyToSocketSettings(x -> {
                                x.connectTimeout(3000, TimeUnit.MILLISECONDS); // 5000 -> 3000
                                x.readTimeout(20000, TimeUnit.MILLISECONDS); // 3000 -> 2000
                              })
                              .build();
  }

  default Block<Builder> defaultPoolSettings() {
    return x -> {
      x.maxSize(50);
      //x.minSize(50);
      x.maxWaitTime(5000, TimeUnit.MILLISECONDS); // 10000 -> 5000
    };
  }
}
