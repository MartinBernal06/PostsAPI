package com.myapp.dao;

import com.myapp.config.MongoConnection;
import com.myapp.models.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

public class UserDAO {
    private final MongoCollection<Document> collection;

    public UserDAO() {
        MongoDatabase db = MongoConnection.getDatabase();
        this.collection = db.getCollection("users");
    }

    public void insert(User user) {
        Document doc = new Document("name", user.getName())
                .append("email", user.getEmail())
                .append("password", user.getPassword());
        collection.insertOne(doc);
        user.setId(doc.getObjectId("_id").toString());
    }

    public User findByEmail(String email) {
        Document doc = collection.find(Filters.eq("email", email)).first();
        return doc != null ? documentToUser(doc) : null;
    }

    public User findById(String id) {
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? documentToUser(doc) : null;
    }

    private User documentToUser(Document doc) {
        User user = new User();
        user.setId(doc.getObjectId("_id").toString());
        user.setName(doc.getString("name"));
        user.setEmail(doc.getString("email"));
        user.setPassword(doc.getString("password"));
        return user;
    }
}
