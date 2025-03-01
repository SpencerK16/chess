package java.service;

import dataaccess.UserDAO;
import dataaccess.DataAccessException;
import model.UserData;
import request.LoginRequest;
import results.LoginResult;
import service.LoginService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    private UserDAO userDAO;

    /**
     * Sets up the testing environment.
     */
    @BeforeEach
    void setup() {
        // Initialize DAO and test data
        userDAO = new UserDAO();

        // Clear the user table
        try {
            userDAO.clear();

        } catch (DataAccessException d) {
            fail("Clearing database threw: " + d.getMessage());
        }

        // Add a test user for the login tests
        try {
            UserData user = new UserData("spencer", "schultz", "spencerschultz@gmail.com");
            userDAO.insertUser(user);
        } catch (DataAccessException e) {
            fail("Inserting test user failed: " + e.getMessage());
        }
    }

    /**
     * Cleans up the test environment after each test.
     */
    @AfterEach
    void cleanup() {
        try {
            // Clear the user table
            userDAO.clear();

        } catch (DataAccessException d) {
            fail("Clearing database threw: " + d.getMessage());
        }
    }

    /**
     * Positive test case which tests logging in with correct credentials.
     */
    @Test
    @DisplayName("Login with Correct Credentials")
    void loginWithCorrectCredentials() {
        // Prepare the login request with valid credentials
        LoginRequest request = new LoginRequest("spencer", "schultz");

        // Execute the login service
        LoginService service = new LoginService(request);
        LoginResult result = service.login();

        // Assert that the login is successful and we have an authToken
        assertTrue(result.success());
        assertNotNull(result.authToken());
        assertEquals("spencer", result.username());
    }

    /**
     * Negative test case which tests logging in with incorrect credentials.
     */
    @Test
    @DisplayName("Login with Incorrect Password")
    void loginWithIncorrectPassword() {
        // Prepare the login request with incorrect password
        LoginRequest request = new LoginRequest("spencer", "wrongpassword");

        // Execute the login service
        LoginService service = new LoginService(request);
        LoginResult result = service.login();

        // Assert that the login fails (unauthorized)
        assertFalse(result.success());
        assertEquals("Error: unauthorized", result.message());
    }

    /**
     * Negative test case which tests logging in with a non-existing username.
     */
    @Test
    @DisplayName("Login with Non-Existing Username")
    void loginWithNonExistingUsername() {
        // Prepare the login request with a non-existing username
        LoginRequest request = new LoginRequest("nonexistentuser", "anyPassword");

        // Execute the login service
        LoginService service = new LoginService(request);
        LoginResult result = service.login();

        // Assert that the login fails (unauthorized)
        assertFalse(result.success());
        assertEquals("Error: unauthorized", result.message());
    }
}
