package com.myapp.dao;

import com.myapp.config.MongoConnection;
import com.myapp.models.Comment;
import com.myapp.models.Post;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

public class PostDAO {
    private final MongoCollection<Document> collection;

    public PostDAO() {
        MongoDatabase db = MongoConnection.getDatabase();
        this.collection = db.getCollection("posts");
    }

    public void insert(Post post) {
        Document doc = new Document("title", post.getTitle())
                .append("content", post.getContent())
                .append("userId", new ObjectId(post.getUserId()))
                .append("authorName", post.getAuthorName())
                .append("createdAt", new Date())
                .append("comments", new ArrayList<>());
        collection.insertOne(doc);
        post.setId(doc.getObjectId("_id").toString());
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        for (Document doc : collection.find()) {
            posts.add(documentToPost(doc));
        }
        return posts;
    }

    public Post findById(String id) {
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? documentToPost(doc) : null;
    }

    private Post documentToPost(Document doc) {
        Post post = new Post();
        post.setId(doc.getObjectId("_id").toString());
        post.setTitle(doc.getString("title"));
        post.setContent(doc.getString("content"));
        post.setUserId(doc.getObjectId("userId").toString());
        post.setAuthorName(doc.getString("authorName"));
        post.setCreatedAt(doc.getDate("createdAt"));

        List<Document> commentDocs = (List<Document>) doc.get("comments");
        if (commentDocs != null) {
            List<Comment> comments = new ArrayList<>();
            for (Document cDoc : commentDocs) {
                Comment comment = new Comment();
                comment.setId(cDoc.getObjectId("_id").toString());
                comment.setContent(cDoc.getString("content"));
                comment.setUserId(cDoc.getObjectId("userId").toString());
                comment.setAuthorName(cDoc.getString("authorName"));
                comment.setCreatedAt(cDoc.getDate("createdAt"));
                comments.add(comment);
            }
            post.setComments(comments);
        }
        return post;
    }
}
