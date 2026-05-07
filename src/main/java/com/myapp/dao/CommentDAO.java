package com.myapp.dao;

import com.myapp.config.MongoConnection;
import com.myapp.models.Comment;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import java.util.Date;
import org.bson.Document;
import org.bson.types.ObjectId;

public class CommentDAO {
    private final MongoCollection<Document> collection;

    public CommentDAO() {
        MongoDatabase db = MongoConnection.getDatabase();
        this.collection = db.getCollection("posts");
    }

    public void addComment(String postId, Comment comment) {
        Document commentDoc = new Document("_id", new ObjectId())
                .append("content", comment.getContent())
                .append("userId", new ObjectId(comment.getUserId()))
                .append("authorName", comment.getAuthorName())
                .append("createdAt", new Date());

        collection.updateOne(
                Filters.eq("_id", new ObjectId(postId)),
                Updates.push("comments", commentDoc)
        );
        
        comment.setId(commentDoc.getObjectId("_id").toString());
    }
}
