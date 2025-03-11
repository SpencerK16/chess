//package dataaccess;
//
//import model.AuthData;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class AuthDAOTest {
//
//    private AuthDAO authDAO;
//
//    @BeforeEach
//    void setup() {
//        authDAO = new AuthDAO();
//        try {
//            authDAO.clear(); // Clear the database before each test
//        } catch (DataAccessException e) {
//            fail("Error clearing the database during setup: " + e.getMessage());
//        }
//    }
//
//    @AfterEach
//    void cleanup() {
//        try {
//            authDAO.clear(); // Clear the database after each test
//        } catch (DataAccessException e) {
//            fail("Error clearing the database during cleanup: " + e.getMessage());
//        }
//    }
//
//    @Test
//    @DisplayName("Insert Auth Token - Positive Test")
//    void insertAuthPositiveTest() {
//        AuthData authData = new AuthData("user1", "authToken1");
//        try {
//            authDAO.insertAuth(authData);
//
//            AuthData retrievedAuth = authDAO.getAuth("authToken1");
//            assertNotNull(retrievedAuth);
//            assertEquals("user1", retrievedAuth.username());
//            assertEquals("authToken1", retrievedAuth.authToken());
//        } catch (DataAccessException e) {
//            fail("Insert auth token failed: " + e.getMessage());
//        }
//    }
//
//    @Test
//    @DisplayName("Insert Auth Token - Negative Test (Duplicate Token)")
//    void insertAuthNegativeTest() {
//        AuthData authData = new AuthData("user1", "authToken1");
//        try {
//            authDAO.insertAuth(authData);
//            authDAO.insertAuth(authData); // Attempt to insert the same token again
//            fail("Expected an exception when inserting a duplicate auth token.");
//        } catch (DataAccessException e) {
//            assertTrue(e.getMessage().contains("Error inserting auth token"));
//        }
//    }
//
//    @Test
//    @DisplayName("Get Auth Token - Positive Test")
//    void getAuthPositiveTest() {
//        AuthData authData = new AuthData("user2", "authToken2");
//        try {
//            authDAO.insertAuth(authData);
//
//            AuthData retrievedAuth = authDAO.getAuth("authToken2");
//            assertNotNull(retrievedAuth);
//            assertEquals("user2", retrievedAuth.username());
//            assertEquals("authToken2", retrievedAuth.authToken());
//        } catch (DataAccessException e) {
//            fail("Get auth token failed: " + e.getMessage());
//        }
//    }
//
//    @Test
//    @DisplayName("Get Auth Token - Negative Test (Non-existent Token)")
//    void getAuthNegativeTest() {
//        try {
//            authDAO.getAuth("nonexistentToken");
//            fail("Expected an exception when retrieving a non-existent auth token.");
//        } catch (DataAccessException e) {
//            assertTrue(e.getMessage().contains("Auth token doesn't exist"));
//        }
//    }
//
//    @Test
//    @DisplayName("Get Username by Auth Token - Positive Test")
//    void getUserPositiveTest() {
//        AuthData authData = new AuthData("user3", "authToken3");
//        try {
//            authDAO.insertAuth(authData);
//
//            String username = authDAO.getUser("authToken3");
//            assertNotNull(username);
//            assertEquals("user3", username);
//        } catch (DataAccessException e) {
//            fail("Get username by auth token failed: " + e.getMessage());
//        }
//    }
//
//    @Test
//    @DisplayName("Get Username by Auth Token - Negative Test (Non-existent Token)")
//    void getUserNegativeTest() {
//        try {
//            authDAO.getUser("nonexistentToken");
//            fail("Expected an exception when retrieving a username for a non-existent token.");
//        } catch (DataAccessException e) {
//            assertTrue(e.getMessage().contains("Auth token doesn't exist"));
//        }
//    }
//
//    @Test
//    @DisplayName("Delete Auth Token - Positive Test")
//    void deleteAuthPositiveTest() {
//        AuthData authData = new AuthData("user4", "authToken4");
//        try {
//            authDAO.insertAuth(authData);
//            authDAO.deleteAuth("authToken4");
//
//            assertThrows(DataAccessException.class, () -> authDAO.getAuth("authToken4"));
//        } catch (DataAccessException e) {
//            fail("Delete auth token failed: " + e.getMessage());
//        }
//    }
//
//    @Test
//    @DisplayName("Delete Auth Token - Negative Test (Non-existent Token)")
//    void deleteAuthNegativeTest() {
//        try {
//            authDAO.deleteAuth("nonexistentToken");
//        } catch (DataAccessException e) {
//            fail("Delete auth token should not throw an exception for a non-existent token.");
//        }
//    }
//
//    @Test
//    @DisplayName("Clear Auth Tokens - Positive Test")
//    void clearAuthTokensPositiveTest() {
//        AuthData authData1 = new AuthData("user5", "authToken5");
//        AuthData authData2 = new AuthData("user6", "authToken6");
//        try {
//            authDAO.insertAuth(authData1);
//            authDAO.insertAuth(authData2);
//
//            authDAO.clear();
//
//            assertThrows(DataAccessException.class, () -> authDAO.getAuth("authToken5"));
//            assertThrows(DataAccessException.class, () -> authDAO.getAuth("authToken6"));
//        } catch (DataAccessException e) {
//            fail("Clear auth tokens failed: " + e.getMessage());
//        }
//    }
//}
