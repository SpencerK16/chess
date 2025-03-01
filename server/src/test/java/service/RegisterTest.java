package java.service;


import dataaccess.UserDAO;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import results.RegisterResult;
import service.RegisterService;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {
    private UserDAO userDAO;

    @BeforeEach
    void setup() {
        userDAO = new UserDAO();

        try {
            userDAO.clear();

        } catch (DataAccessException d) {
            fail("Clearing database threw: " + d.getMessage());
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
    @DisplayName("Register Single User")
    void singleUserRegistrationTest() {

        RegisterRequest request = new RegisterRequest("spencer", "schultz", "spencerschultz@gmail.com");

        RegisterService service = new RegisterService(request);
        RegisterResult result = service.register();

        assertTrue(result.success());
        assertNotNull(result.authToken());

        assertEquals("spencer", result.username());
        assertNotNull(result.message());
    }

    @Test
    @DisplayName("Blank Password Testing")
    void wrongPasswordTesting() {
        RegisterRequest request = new RegisterRequest("spencer", "", "spencerschultz@gmail.com");

        RegisterService service = new RegisterService(request);
        RegisterResult result = service.register();

        assertFalse(result.success());
    }

    @Test
    @DisplayName("Username Already Taken Testing")
    void usernameAlreadyTakenTesting() {
        RegisterRequest request = new RegisterRequest("spencer", "schultz", "spencerschultz@gmail.com");

        RegisterService service = new RegisterService(request);
        RegisterResult result = service.register();

        request = new RegisterRequest("spencer", "schultz", "spencerschultz@gmail.com");

        RegisterService dService = new RegisterService(request);
        RegisterResult dResult = service.register();

        assertFalse(dResult.success());
    }
}
