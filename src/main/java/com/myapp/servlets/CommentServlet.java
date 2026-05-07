package com.myapp.servlets;

import com.myapp.models.Comment;
import com.myapp.services.CommentService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/comments")
public class CommentServlet extends BaseServlet {
    private final CommentService commentService = new CommentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = (String) request.getAttribute("userId");
        
        // Expecting { "postId": "...", "content": "..." }
        CommentRequest commentReq = gson.fromJson(request.getReader(), CommentRequest.class);
        
        if (commentReq.getPostId() == null || commentReq.getContent() == null) {
            sendError(response, "Missing postId or content", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            Comment newComment = commentService.addComment(commentReq.getPostId(), commentReq.getContent(), userId);
            sendResponse(response, newComment, HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            sendError(response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // Helper class for incoming comment request
    private static class CommentRequest {
        private String postId;
        private String content;

        public String getPostId() { return postId; }
        public String getContent() { return content; }
    }
}
