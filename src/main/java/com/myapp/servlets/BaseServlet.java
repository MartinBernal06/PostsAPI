package com.myapp.servlets;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {
    protected final Gson gson = new Gson();

    protected void sendResponse(HttpServletResponse response, Object data, int status) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        response.getWriter().write(gson.toJson(data));
    }

    protected void sendError(HttpServletResponse response, String message, int status) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
