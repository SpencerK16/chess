package service;

import chess.ChessGame;
import dataaccess.UserDAO;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.UserData;
import model.GameData;
import model.AuthData;
import results.ClearResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearTest {

    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private ClearService clearService;

    @BeforeEach
    void setup() {
        userDAO = new UserDAO();
        gameDAO = new GameDAO();
        authDAO = new AuthDAO();
        clearService = new ClearService(userDAO, gameDAO, authDAO);
    }

    @AfterEach
    void cleanup() {
        try {
            userDAO.clear();
            gameDAO.clear();
            authDAO.clear();
        } catch (DataAccessException e) {
            fail("Error clearing DAOs: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test Successful Data Clearing")
    void testClearDataSuccess() {
        try {
            userDAO.insertUser(new UserData("user1", "password", "spencer@gmail.com"));
            gameDAO.insertGame(new GameData(1, "player1", "player2", "game1", new ChessGame()));
            authDAO.insertAuth(new AuthData("user1", "authToken1"));

            assertNotNull(userDAO.getUser("user1"));
            assertNotNull(gameDAO.getGame(1));
            assertNotNull(authDAO.getAuth("authToken1"));

            ClearResult result = clearService.clear();

            assertTrue(result.success());
            assertEquals("Data cleared successfully.", result.message());

            assertThrows(DataAccessException.class, () -> userDAO.getUser("user1"));
            assertThrows(DataAccessException.class, () -> gameDAO.getGame(1));
            assertThrows(DataAccessException.class, () -> authDAO.getAuth("authToken1"));
        } catch (DataAccessException e) {
            fail("Error during test: " + e.getMessage());
        }
    }
}