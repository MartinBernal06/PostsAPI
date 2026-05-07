package com.myapp.config;

import com.myapp.dao.UserDAO;
import com.myapp.dao.PostDAO;
import com.myapp.dao.CommentDAO;
import com.myapp.models.User;
import com.myapp.models.Post;
import com.myapp.models.Comment;
import com.myapp.utils.PasswordUtils;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DatabaseSeeder implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Iniciando semilla de base de datos...");
        seed();
    }

    public void seed() {
        UserDAO userDAO = new UserDAO();
        PostDAO postDAO = new PostDAO();
        CommentDAO commentDAO = new CommentDAO();

        // Verificar si ya hay usuarios
        if (userDAO.findByEmail("martin@test.com") == null) {
            System.out.println("Base de datos vacia. Insertando datos de prueba...");

            // 1. Crear Usuarios
            User martin = new User("Martin", "martin@test.com", PasswordUtils.hashPassword("123456"));
            userDAO.insert(martin);

            User juan = new User("Juan", "juan@test.com", PasswordUtils.hashPassword("123456"));
            userDAO.insert(juan);

            // 2. Crear un Post para Martin
            Post post = new Post();
            post.setTitle("Mi primer post");
            post.setContent("Este es un post generado automaticamente como semilla.");
            post.setUserId(martin.getId());
            post.setAuthorName(martin.getName());
            postDAO.insert(post);

            // 3. Crear un Comentario de Juan en el post de Martin
            Comment comment = new Comment();
            comment.setContent("¡Buen inicio de proyecto!");
            comment.setUserId(juan.getId());
            comment.setAuthorName(juan.getName());
            commentDAO.addComment(post.getId(), comment);

            System.out.println("Semilla ejecutada con exito.");
        } else {
            System.out.println("La base de datos ya contiene datos, saltando semilla.");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        MongoConnection.close();
    }
}
