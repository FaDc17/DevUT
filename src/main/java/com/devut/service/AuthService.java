package com.devut.service;

import com.devut.dto.LoginResult;
import com.devut.model.User;
import com.devut.repository.UserRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final Map<String, RegisteredUser> registeredUsers = new ConcurrentHashMap<>();
    private final UserRepository users;

    public AuthService() {
        this.users = null;
    }

    @Autowired
    public AuthService(UserRepository users) {
        this.users = users;
    }

    public LoginResult authenticate(String username, String password) {
        if (username == null || username.isBlank()) {
            return new LoginResult(false, false, null, "Ingresa tu usuario o correo.");
        }
        if (password == null || password.isBlank()) {
            return new LoginResult(false, false, null, "Ingresa tu contraseña.");
        }

        String normalizedUsername = username.trim().toLowerCase();
        if ("admin".equals(normalizedUsername) || "admin@devut.test".equals(normalizedUsername)) {
            if ("123456".equals(password)) {
                return new LoginResult(true, true, "Admin", null);
            }
            return new LoginResult(false, false, null, "Usuario o contraseña incorrectos.");
        }

        RegisteredUser user = findUser(normalizedUsername);
        if (user != null && user.password().equals(password)) {
            return new LoginResult(true, false, user.name(), null);
        }
        return new LoginResult(false, false, null, "Usuario o contraseña incorrectos.");
    }

    public LoginResult register(String firstName, String lastName, String email, String password, String repeatedPassword) {
        if (firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank()) {
            return new LoginResult(false, false, null, "Ingresa tu nombre y apellido.");
        }
        if (email == null || email.isBlank()) {
            return new LoginResult(false, false, null, "Ingresa tu correo electrónico.");
        }
        if (password == null || password.length() < 8) {
            return new LoginResult(false, false, null, "La contraseña debe tener mínimo 8 caracteres.");
        }
        if (!password.equals(repeatedPassword)) {
            return new LoginResult(false, false, null, "Las contraseñas no coinciden.");
        }

        String normalizedEmail = email.trim().toLowerCase();
        String cleanFirstName = firstName.trim();
        String cleanLastName = lastName.trim();
        if (users != null && users.findByEmail(normalizedEmail).isPresent()) {
            return new LoginResult(false, false, null, "Ya existe una cuenta con este correo.");
        }
        if (users == null) {
            registeredUsers.put(normalizedEmail, new RegisteredUser(cleanFirstName, cleanLastName, normalizedEmail, password));
        } else {
            User user = new User();
            user.setFirstName(cleanFirstName);
            user.setLastName(cleanLastName);
            user.setEmail(normalizedEmail);
            user.setPassword(password);
            users.save(user);
        }
        return new LoginResult(true, false, cleanFirstName + " " + cleanLastName, null);
    }

    public UserProfile getProfile(String email) {
        RegisteredUser user = findUser(email == null ? "" : email.trim().toLowerCase());
        return user == null ? null : new UserProfile(user.firstName(), user.lastName(), user.email());
    }

    public boolean updateProfile(String currentEmail, String firstName, String lastName, String email) {
        if (currentEmail == null || firstName == null || firstName.isBlank()
                || lastName == null || lastName.isBlank() || email == null || email.isBlank()) {
            return false;
        }
        String normalizedCurrentEmail = currentEmail.trim().toLowerCase();
        String normalizedEmail = email.trim().toLowerCase();
        if (users == null) {
            RegisteredUser current = registeredUsers.remove(normalizedCurrentEmail);
            if (current == null) return false;
            registeredUsers.put(normalizedEmail, new RegisteredUser(firstName.trim(), lastName.trim(), normalizedEmail, current.password()));
            return true;
        }
        User current = users.findByEmail(normalizedCurrentEmail).orElse(null);
        if (current == null) return false;
        if (!normalizedCurrentEmail.equals(normalizedEmail) && users.findByEmail(normalizedEmail).isPresent()) return false;
        current.setFirstName(firstName.trim());
        current.setLastName(lastName.trim());
        current.setEmail(normalizedEmail);
        users.save(current);
        return true;
    }

    private RegisteredUser findUser(String email) {
        if (users == null) {
            return registeredUsers.get(email);
        }
        return users.findByEmail(email).map(user -> new RegisteredUser(
                user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword())).orElse(null);
    }

    public record UserProfile(String firstName, String lastName, String email) {}

    private record RegisteredUser(String firstName, String lastName, String email, String password) {
        String name() { return firstName + " " + lastName; }
    }
}
