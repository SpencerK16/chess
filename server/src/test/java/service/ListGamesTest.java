package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import request.ListGamesRequest;
import results.ListGamesResult;
import static org.junit.jupiter.api.Assertions.*;

public class ListGamesTest {
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private ListGamesRequest request;
    private ListGamesService service;

    @BeforeEach
    void setup() {
        authDAO = new AuthDAO();
        gameDAO = new GameDAO();

        try {
            authDAO.clear();
            gameDAO.clear();
        } catch (DataAccessException e) {
            fail("Failed to clear database: " + e.getMessage());
        }
    }

    @AfterEach
    void cleanup() {
        try {
            authDAO.clear();
            gameDAO.clear();
        } catch (DataAccessException e) {
            fail("Failed to clear database: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("List Games - Valid Token")
    void listGamesValidTokenTest() throws DataAccessException {
        // Insert user and game data
        String validAuthToken = "validAuthToken";
        String username = "user1";

        GameData game1 = new GameData(1, "user1", "user2", "Chess Game 1", new ChessGame());
        GameData game2 = new GameData(2, "user1", "user3", "Chess Game 2", new ChessGame());

        authDAO.insertAuth(new AuthData(username, validAuthToken));
        gameDAO.insertGame(game1);
        gameDAO.insertGame(game2);

        request = new ListGamesRequest(validAuthToken);
        service = new ListGamesService(request, authDAO, gameDAO);

        ListGamesResult result = service.listGames();

        assertTrue(result.success());
        assertNotNull(result.games());
        assertEquals(2, result.games().size()); // As there are two games inserted
        assertEquals("Games retrieved successfully", result.message());
    }

    @Test
    @DisplayName("List Games - No Games Found")
    void listGamesNoGamesFoundTest() throws DataAccessException {
        // Insert user data (with no games)
        String validAuthToken = "validAuthTokenNoGames";
        String username = "user3";

        authDAO.insertAuth(new AuthData(username, validAuthToken));

        request = new ListGamesRequest(validAuthToken);
        service = new ListGamesService(request, authDAO, gameDAO);

        ListGamesResult result = service.listGames();

        assertFalse(result.success());
        assertNull(result.games());
        assertEquals("No games found for the user", result.message());
    }
}