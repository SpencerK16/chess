package java.service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import request.LogoutRequest;
import results.LogoutResult;
import service.LogoutService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutTest {

    private AuthDAO authDAO;

    /**
     * Sets up the testing environment.
     */
    @BeforeEach
    void setup() {
        // Initialize the AuthDAO
        authDAO = new AuthDAO();

        // Clear the auth table
        try {
            authDAO.clear();
        } catch (DataAccessException d) {
            fail("Clearing database threw: " + d.getMessage());
        }
    }

    /**
     * Cleans up the test environment after each test.
     */
    @AfterEach
    void cleanup() {
        try {
            // Clear the auth table
            authDAO.clear();
        } catch (DataAccessException d) {
            fail("Clearing database threw: " + d.getMessage());
        }
    }

    /**
     * Positive test case for logging out with a valid authToken.
     */
    @Test
    @DisplayName("Logout with Valid authToken")
    void logoutWithValidToken() {
        // Create a valid authToken and AuthData for the user
        String validAuthToken = "validAuthToken123";
        String username = "spencer";

        // Create AuthData object (username and token)
        AuthData authData = new AuthData(username, validAuthToken);

        // Simulate inserting this AuthData into the database
        try {
            authDAO.insertAuth(authData);
        } catch (DataAccessException e) {
            fail("Error inserting authData: " + e.getMessage());
        }

        // Create the logout request using the valid authToken
        LogoutRequest logoutRequest = new LogoutRequest(validAuthToken);

        // Create the LogoutService with the request and authDAO
        LogoutService logoutService = new LogoutService(logoutRequest, authDAO);

        // Perform the logout
        LogoutResult logoutResult = logoutService.logout();

        // Assert logout was successful
        assertTrue(logoutResult.success());
        assertEquals("Logout successful", logoutResult.message());
    }

    /**
     * Negative test case for logging out with an invalid authToken.
     */
    @Test
    @DisplayName("Logout with Invalid authToken")
    void logoutWithInvalidToken() {
        // Create an invalid authToken (this token does not exist in the system)
        String invalidAuthToken = "invalidAuthToken123";

        // Create the logout request using the invalid authToken
        LogoutRequest logoutRequest = new LogoutRequest(invalidAuthToken);

        // Create the LogoutService with the request and authDAO
        LogoutService logoutService = new LogoutService(logoutRequest, authDAO);

        // Perform the logout
        LogoutResult logoutResult = logoutService.logout();

        // Assert that logout fails with the expected error message
        assertFalse(logoutResult.success());
        assertEquals("Error: Invalid authToken", logoutResult.message());
    }
}
