package com.car.server;

//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import jakarta.annotation.PostConstruct;
//import org.bson.Document;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
//import static com.mongodb.client.model.Filters.eq;

@SpringBootApplication(scanBasePackages = "com.car.server")
@EnableMongoAuditing // 启用审计功能
public class ServerApplication {

//    @Value("${spring.data.mongodb.uri}")
//    private String mongoUri;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    /*@PostConstruct
    public void init(){
        try(MongoClient mongoClient = MongoClients.create(mongoUri)){
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");

            Document doc = collection.find(eq("title","Back to the Future")).first();
            if(doc != null){
                System.out.println(doc.toJson());
            }else{
                System.out.println("No matching documents found.");
            }
        }
    }*/

}
