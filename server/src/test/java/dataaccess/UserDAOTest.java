//package dataaccess;
//
//import model.UserData;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class UserDAOTest {
//
//    private UserDAO userDAO;
//
//    @BeforeEach
//    void setup() {
//        userDAO = new UserDAO();
//        try {
//            userDAO.clear(); // Clear the database before each test
//        } catch (DataAccessException e) {
//            fail("Error clearing the database during setup: " + e.getMessage());
//        }
//    }
//
//    @AfterEach
//    void cleanup() {
//        try {
//            userDAO.clear(); // Clear the database after each test
//        } catch (DataAccessException e) {
//            fail("Error clearing the database during cleanup: " + e.getMessage());
//        }
//    }
//
//    @Test
//    @DisplayName("Insert User - Positive Test")
//    void insertUserPositiveTest() {
//        UserData user = new UserData("user1", "hashed_password", "user1@example.com");
//        try {
//            userDAO.insertUser(user);
//
//            UserData retrievedUser = userDAO.getUser("user1");
//            assertNotNull(retrievedUser);
//            assertEquals("user1", retrievedUser.username());
//            assertEquals("hashed_password", retrievedUser.password());
//            assertEquals("user1@example.com", retrievedUser.email());
//        } catch (DataAccessException e) {
//            fail("Insert user failed: " + e.getMessage());
//        }
//    }
//
//    @Test
//    @DisplayName("Insert User - Negative Test (Duplicate Username)")
//    void insertUserNegativeTest() {
//        UserData user = new UserData("user1", "hashed_password", "user1@example.com");
//        try {
//            userDAO.insertUser(user);
//            userDAO.insertUser(user); // Attempting to insert the same user again should fail
//            fail("Expected an exception when inserting a duplicate user.");
//        } catch (DataAccessException e) {
//            assertTrue(e.getMessage().contains("Error inserting user"));
//        }
//    }
//
//    @Test
//    @DisplayName("Get User - Positive Test")
//    void getUserPositiveTest() {
//        UserData user = new UserData("user2", "hashed_password_2", "user2@example.com");
//        try {
//            userDAO.insertUser(user);
//
//            UserData retrievedUser = userDAO.getUser("user2");
//            assertNotNull(retrievedUser);
//            assertEquals("user2", retrievedUser.username());
//            assertEquals("hashed_password_2", retrievedUser.password());
//            assertEquals("user2@example.com", retrievedUser.email());
//        } catch (DataAccessException e) {
//            fail("Get user failed: " + e.getMessage());
//        }
//    }
//
//    @Test
//    @DisplayName("Get User - Negative Test (Non-existent User)")
//    void getUserNegativeTest() {
//        try {
//            userDAO.getUser("nonexistent");
//            fail("Expected an exception when retrieving a non-existent user.");
//        } catch (DataAccessException e) {
//            assertTrue(e.getMessage().contains("User doesn't exist"));
//        }
//    }
//
//    @Test
//    @DisplayName("Username Exists - Positive Test")
//    void usernameExistsPositiveTest() {
//        UserData user = new UserData("user3", "hashed_password_3", "user3@example.com");
//        try {
//            userDAO.insertUser(user);
//
//            assertTrue(userDAO.usernameExists("user3"));
//        } catch (DataAccessException e) {
//            fail("Username exists check failed: " + e.getMessage());
//        }
//    }
//
//    @Test
//    @DisplayName("Username Exists - Negative Test")
//    void usernameExistsNegativeTest() {
//        try {
//            assertFalse(userDAO.usernameExists("nonexistent_user"));
//        } catch (DataAccessException e) {
//            fail("Username exists check failed: " + e.getMessage());
//        }
//    }
//
//    @Test
//    @DisplayName("Clear Users - Positive Test")
//    void clearUsersPositiveTest() {
//        UserData user = new UserData("user4", "hashed_password_4", "user4@example.com");
//        try {
//            userDAO.insertUser(user);
//            userDAO.clear();
//
//            assertFalse(userDAO.usernameExists("user4")); // After clearing, the user should not exist
//        } catch (DataAccessException e) {
//            fail("Clear users failed: " + e.getMessage());
//        }
//    }
//}
