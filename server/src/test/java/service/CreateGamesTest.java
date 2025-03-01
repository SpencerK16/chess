package java.service;

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
import service.CreateGameService;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGamesTest {

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
        AuthData authData = new AuthData("validAuthToken", "player1");
        try {
            authDAO.insertAuth(authData);

            CreateGameRequest request = new CreateGameRequest("validAuthToken", "Chess Game");

            CreateGameService service = new CreateGameService(request, null, authDAO, gameDAO);
            CreateGameResult result = service.createGame();

            assertTrue(result.success());
            assertNotNull(result.GameID());
            assertEquals("Game created successfully", result.message());

            GameData game = gameDAO.getGame(Integer.parseInt(result.GameID()));
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
        assertEquals("Error: Unauthorized", result.message());
    }


}
