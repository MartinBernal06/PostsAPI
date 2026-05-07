package com.myapp.services;

import com.myapp.dao.UserDAO;
import com.myapp.models.User;
import com.myapp.utils.JWTUtils;
import com.myapp.utils.PasswordUtils;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    /**
     * Registra un nuevo usuario en el sistema.
     */
    public User register(String name, String email, String password) throws Exception {
        if (userDAO.findByEmail(email) != null) {
            throw new Exception("El correo ya esta registrado");
        }
        // Encriptar password antes de guardar
        User user = new User(name, email, PasswordUtils.hashPassword(password));
        userDAO.insert(user);
        return user;
    }

    /**
     * Valida credenciales y genera un token JWT.
     */
    public String login(String email, String password) throws Exception {
        User user = userDAO.findByEmail(email);
        if (user == null || !PasswordUtils.checkPassword(password, user.getPassword())) {
            throw new Exception("Credenciales invalidas");
        }
        // Generar JWT con el ID del usuario y su email
        return JWTUtils.generateToken(user.getId(), user.getEmail());
    }

    public User getUserById(String id) {
        return userDAO.findById(id);
    }
}
