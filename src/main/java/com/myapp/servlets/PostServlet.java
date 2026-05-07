package com.myapp.servlets;

import com.myapp.models.Post;
import com.myapp.services.PostService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/posts/*")
public class PostServlet extends BaseServlet {
    private final PostService postService = new PostService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();

        if (path == null || path.equals("/")) {
            // GET /api/posts
            List<Post> posts = postService.getAllPosts();
            sendResponse(response, posts, HttpServletResponse.SC_OK);
        } else {
            // GET /api/posts/{id}
            String id = path.substring(1);
            Post post = postService.getPostById(id);
            if (post != null) {
                sendResponse(response, post, HttpServletResponse.SC_OK);
            } else {
                sendError(response, "Post not found", HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = (String) request.getAttribute("userId");
        Post postData = gson.fromJson(request.getReader(), Post.class);
        
        try {
            Post newPost = postService.createPost(postData.getTitle(), postData.getContent(), userId);
            sendResponse(response, newPost, HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            sendError(response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
