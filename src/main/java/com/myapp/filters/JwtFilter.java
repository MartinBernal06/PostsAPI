package com.myapp.filters;

import com.myapp.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String method = httpRequest.getMethod();
        String path = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String relativePath = path.substring(contextPath.length());

        // Permitir GET /api/posts sin token (público)
        if (method.equalsIgnoreCase("GET") && (relativePath.equals("/api/posts") || relativePath.equals("/api/posts/"))) {
            chain.doFilter(request, response);
            return;
        }

        // Obtener el header de Authorization
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\": \"Falta el token de autorizacion\"}");
            return;
        }

        // Validar el token JWT
        String token = authHeader.substring(7);
        Claims claims = JWTUtils.validateToken(token);

        if (claims == null) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\": \"Token invalido o expirado\"}");
            return;
        }

        // Guardar el userId en los atributos del request para uso posterior en Servlets
        httpRequest.setAttribute("userId", claims.getSubject());
        chain.doFilter(request, response);
    }
}
