package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import dataaccess.DataAccessException;
import model.UserData;
import request.LoginRequest;
import results.LoginResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    private UserDAO userDAO;
    private AuthDAO authDAO;

    @BeforeEach
    void setup() {
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        try {
            userDAO.clear();

        } catch (DataAccessException d) {
            fail("Clearing database threw: " + d.getMessage());
        }

        try {
            UserData user = new UserData("spencer", "schultz", "spencerschultz@gmail.com");
            userDAO.insertUser(user);
        } catch (DataAccessException e) {
            fail("Inserting test user failed: " + e.getMessage());
        }
    }

    @AfterEach
    void cleanup() {
        try {
            userDAO.clear();

        } catch (DataAccessException d) {
            fail("Clearing database threw: " + d.getMessage());
        }
    }

    @Test
    @DisplayName("Login with Correct Credentials")
    void loginWithCorrectCredentials() {
        LoginRequest request = new LoginRequest("spencer", "schultz");

        LoginService service = new LoginService(request, userDAO, authDAO);
        LoginResult result = service.login();

        assertTrue(result.success());
        assertNotNull(result.authToken());
        assertEquals("spencer", result.username());
    }

    @Test
    @DisplayName("Login with Incorrect Password")
    void loginWithIncorrectPassword() {
        LoginRequest request = new LoginRequest("spencer", "wrongpassword");

        LoginService service = new LoginService(request, userDAO, authDAO);
        LoginResult result = service.login();

        assertFalse(result.success());
        assertEquals("error: unauthorized", result.message().toLowerCase());
    }
}
