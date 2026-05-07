package com.myapp.services;

import com.myapp.dao.CommentDAO;
import com.myapp.models.Comment;
import com.myapp.models.User;

public class CommentService {
    private final CommentDAO commentDAO = new CommentDAO();
    private final AuthService authService = new AuthService();

    public Comment addComment(String postId, String content, String userId) {
        User user = authService.getUserById(userId);
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUserId(userId);
        comment.setAuthorName(user.getName());
        commentDAO.addComment(postId, comment);
        return comment;
    }
}
