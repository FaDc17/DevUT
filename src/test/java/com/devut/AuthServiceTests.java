package com.devut;

import com.devut.dto.LoginResult;
import com.devut.service.AuthService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTests {
    private final AuthService service = new AuthService();

    @Test
    void validatesDemoCredentials() {
        LoginResult valid = service.authenticate("admin", "123456");
        LoginResult invalid = service.authenticate("admin", "incorrecta");
        assertTrue(valid.successful());
        assertTrue(valid.admin());
        assertFalse(invalid.successful());
        assertEquals("Usuario o contraseña incorrectos.", invalid.error());
    }

    @Test
    void validatesRequiredFields() {
        assertEquals("Ingresa tu usuario o correo.", service.authenticate("", "123456").error());
        assertEquals("Ingresa tu contraseña.", service.authenticate("admin", "").error());
    }

    @Test
    void registersAndReturnsDisplayName() {
        LoginResult result = service.register("María", "López", "maria@example.com", "Clave123!", "Clave123!");
        assertTrue(result.successful());
        assertEquals("María López", result.displayName());
        assertTrue(service.authenticate("maria@example.com", "Clave123!").successful());
    }
}
