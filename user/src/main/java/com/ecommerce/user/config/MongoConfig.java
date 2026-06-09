package com.ecommerce.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Bean
    public MongoClient mongoClient() {
        System.out.println("Connecting to MongoDB Atlas with URI: " + mongoUri);
        return MongoClients.create(mongoUri);
        /*
        ServerAddress serverAddress = new ServerAddress("localhost", 27017);

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                    builder.hosts(java.util.Arrays.asList(serverAddress))
                )
                .credential(
                    com.mongodb.MongoCredential.createScramSha256Credential(
                        "root_admin",
                        "admin",
                        "root_password".toCharArray()
                    )
                )
                .build();

        return MongoClients.create(mongoClientSettings);*/
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoClient, "UserDB"));
    }
}
