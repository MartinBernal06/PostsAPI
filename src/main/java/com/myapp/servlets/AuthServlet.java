package com.myapp.servlets;

import com.myapp.dto.AuthRequest;
import com.myapp.dto.AuthResponse;
import com.myapp.models.User;
import com.myapp.services.AuthService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/auth/*")
public class AuthServlet extends BaseServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();

        if (path == null) {
            sendError(response, "Invalid path", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        AuthRequest authReq = gson.fromJson(request.getReader(), AuthRequest.class);

        try {
            if (path.equals("/register")) {
                User user = authService.register(authReq.getName(), authReq.getEmail(), authReq.getPassword());
                sendResponse(response, user, HttpServletResponse.SC_CREATED);
            } else if (path.equals("/login")) {
                String token = authService.login(authReq.getEmail(), authReq.getPassword());
                sendResponse(response, new AuthResponse(token), HttpServletResponse.SC_OK);
            } else {
                sendError(response, "Not found", HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            sendError(response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
