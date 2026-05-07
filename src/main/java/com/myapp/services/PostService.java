package com.myapp.services;

import com.myapp.dao.PostDAO;
import com.myapp.models.Post;
import com.myapp.models.User;
import java.util.List;

public class PostService {
    private final PostDAO postDAO = new PostDAO();
    private final AuthService authService = new AuthService();

    public Post createPost(String title, String content, String userId) {
        User user = authService.getUserById(userId);
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUserId(userId);
        post.setAuthorName(user.getName());
        postDAO.insert(post);
        return post;
    }

    public List<Post> getAllPosts() {
        return postDAO.findAll();
    }

    public Post getPostById(String id) {
        return postDAO.findById(id);
    }
}
