package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.JoinGameRequest;
import results.JoinGameResult;
import service.JoinGameService;

import static org.junit.jupiter.api.Assertions.*;

public class JoinGameTest {

    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;

    @BeforeEach
    void setup() {
        authDAO = new AuthDAO();
        gameDAO = new GameDAO();
        userDAO = new UserDAO();

        try {
            authDAO.clear();
            gameDAO.clear();
            userDAO.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Successfully Join Game")
    void joinGameSuccessTest() {
        String authToken = "validAuthToken";
        String playerColor = "white";
        String gameID = "1";
        JoinGameRequest request = new JoinGameRequest(authToken, playerColor, gameID);

        String username = "player1";
        GameData game = new GameData(1, null, null, null, new ChessGame());

        try {
            authDAO.insertAuth(new AuthData(username, authToken));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            gameDAO.insertGame(game);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JoinGameService service = new JoinGameService(request, userDAO, authDAO, gameDAO);

        JoinGameResult result = service.joinGame();

        assertTrue(result.success());
        assertEquals("Successfully joined the game.", result.message());
    }

    @Test
    @DisplayName("Fail to Join Game due to Invalid Auth Token")
    void joinGameInvalidAuthTokenTest() {
        String authToken = "invalidAuthToken";
        String playerColor = "black";
        String gameID = "1";
        JoinGameRequest request = new JoinGameRequest(authToken, playerColor, gameID);

        JoinGameService service = new JoinGameService(request, userDAO, authDAO, gameDAO);

        JoinGameResult result = service.joinGame();

        assertFalse(result.success());
        assertEquals("Error: Invalid or expired auth token.", result.message());
    }
}