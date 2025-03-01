package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import request.LogoutRequest;
import results.LogoutResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutTest {
    private AuthDAO authDAO;

    @BeforeEach
    void setup() {
        authDAO = new AuthDAO();

        try {
            authDAO.clear();
        } catch (DataAccessException d) {
            fail("Clearing database threw: " + d.getMessage());
        }
    }

    @AfterEach
    void cleanup() {
        try {
            authDAO.clear();
        } catch (DataAccessException d) {
            fail("Clearing database threw: " + d.getMessage());
        }
    }

    @Test
    @DisplayName("Logout with Valid authToken")
    void logoutWithValidToken() {
        String validAuthToken = "validAuthToken123";
        String username = "spencer";

        AuthData authData = new AuthData(username, validAuthToken);

        try {
            authDAO.insertAuth(authData);
        } catch (DataAccessException e) {
            fail("Error inserting authData: " + e.getMessage());
        }

        LogoutRequest logoutRequest = new LogoutRequest(validAuthToken);

        LogoutService logoutService = new LogoutService(logoutRequest, authDAO);

        LogoutResult logoutResult = logoutService.logout();

        assertTrue(logoutResult.success());
        assertEquals("Logout successful", logoutResult.message());
    }

    @Test
    @DisplayName("Logout with Invalid authToken")
    void logoutWithInvalidToken() {
        String invalidAuthToken = "invalidAuthToken123";

        LogoutRequest logoutRequest = new LogoutRequest(invalidAuthToken);

        LogoutService logoutService = new LogoutService(logoutRequest, authDAO);

        LogoutResult logoutResult = logoutService.logout();

        assertFalse(logoutResult.success());
        assertEquals("Error: AuthToken doesn't exist.", logoutResult.message());
    }
}
