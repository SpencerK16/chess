package java.service;


import dataaccess.UserDAO;
import dataaccess.DataAccessException;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import model.UserData;
import request.RegisterRequest;
import results.RegisterResult;
import service.RegisterService;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {
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
     * Positive test case which tests registering a single user.
     */
    @Test
    @DisplayName("Register Single User")
    void singleUserRegistrationTest() {

        // Create the request and service, and execute the registration
        RegisterRequest request = new RegisterRequest("spencer", "schultz", "spencerschultz@gmail.com");

        RegisterService service = new RegisterService(request);
        RegisterResult result = service.register();

        // The result success should be true and the authtoken, personID, and username should be not null
        assertTrue(result.success());
        assertNotNull(result.authToken());

        assertEquals("spencer", result.username());
        assertNotNull(result.message());
    }

     /*
     * Negative test case which tests a user putting a blank password in.
     */
    @Test
    @DisplayName("Blank Password Testing")
    void wrongPasswordTesting() {
        // Create the request and service, and execute the registration
        RegisterRequest request = new RegisterRequest("spencer", "", "spencerschultz@gmail.com");

        RegisterService service = new RegisterService(request);
        RegisterResult result = service.register();

        // The result success should be true and the authtoken, personID, and username should be not null
        assertFalse(result.success());
    }

    /**
     * Negative test case which tests for duplicate registration.
     */
    @Test
    @DisplayName("Username Already Taken Testing")
    void usernameAlreadyTakenTesting() {
        // Create the request and service, and execute the registration
        RegisterRequest request = new RegisterRequest("spencer", "schultz", "spencerschultz@gmail.com");

        RegisterService service = new RegisterService(request);
        RegisterResult result = service.register();

        request = new RegisterRequest("spencer", "schultz", "spencerschultz@gmail.com");

        RegisterService dService = new RegisterService(request);
        RegisterResult dResult = service.register();

        // The result success should be true and the authtoken, personID, and username should be not null
        assertFalse(dResult.success());
    }
}
