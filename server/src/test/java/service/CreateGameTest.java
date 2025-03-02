package service;

import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.GameData;
import model.AuthData;
import request.CreateGameRequest;
import results.CreateGameResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGameTest {
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    @BeforeEach
    void setup() {
        gameDAO = new GameDAO();
        authDAO = new AuthDAO();
    }

    @AfterEach
    void cleanup() {
        try {
            gameDAO.clear();
            authDAO.clear();
        } catch (DataAccessException e) {
            fail("Clearing database threw: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test Successful Game Creation")
    void testCreateGameSuccess() {
        AuthData authData = new AuthData("player1", "validAuthToken");
        try {
            authDAO.insertAuth(authData);

            CreateGameRequest request = new CreateGameRequest("validAuthToken", "Chess Game");

            CreateGameService service = new CreateGameService(request, null, authDAO, gameDAO);
            CreateGameResult result = service.createGame();

            assertTrue(result.success());
            assertNotNull(result.gameID());
            assertEquals("Game created successfully", result.message());

            GameData game = gameDAO.getGame(Integer.parseInt(result.gameID()));
            assertNotNull(game);
            assertEquals("Chess Game", game.gameName());
        } catch (DataAccessException e) {
            fail("Database error: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test Create Game with Invalid AuthToken")
    void testCreateGameWithInvalidAuthToken() {
        CreateGameRequest request = new CreateGameRequest("invalidAuthToken", "Chess Game");

        CreateGameService service = new CreateGameService(request, null, authDAO, gameDAO);
        CreateGameResult result = service.createGame();

        assertFalse(result.success());
        assertEquals("Error: AuthToken doesn't exist.", result.message());
    }


}
