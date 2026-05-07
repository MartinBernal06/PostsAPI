package com.myapp.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "posts_db";
    private static MongoClient mongoClient = null;

    private MongoConnection() {}

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            try {
                mongoClient = MongoClients.create(CONNECTION_STRING);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error connecting to MongoDB");
            }
        }
        return mongoClient.getDatabase(DATABASE_NAME);
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }
}
