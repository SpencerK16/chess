package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.GameListData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameDAOTest {

    private GameDAO gameDAO;

    @BeforeEach
    void setup() {
        gameDAO = new GameDAO();
        try {
            gameDAO.clear(); // Clear the database before each test
        } catch (DataAccessException e) {
            fail("Error clearing the database during setup: " + e.getMessage());
        }
    }

    @AfterEach
    void cleanup() {
        try {
            gameDAO.clear(); // Clear the database after each test
        } catch (DataAccessException e) {
            fail("Error clearing the database during cleanup: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Insert Game - Positive Test")
    void insertGamePositiveTest() {
        GameData game = new GameData(1, "whitePlayer", "blackPlayer", "game1", new ChessGame());
        try {
            gameDAO.insertGame(game);

            GameData retrievedGame = gameDAO.getGame(1);
            assertNotNull(retrievedGame);
            assertEquals(1, retrievedGame.gameID());
            assertEquals("whitePlayer", retrievedGame.whiteUsername());
            assertEquals("blackPlayer", retrievedGame.blackUsername());
            assertEquals("game1", retrievedGame.gameName());
        } catch (DataAccessException e) {
            fail("Insert game failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Insert Game - Negative Test (Duplicate ID)")
    void insertGameNegativeTest() {
        GameData game = new GameData(2, "whitePlayer", "blackPlayer", "game2", new ChessGame());
        try {
            gameDAO.insertGame(game);
            gameDAO.insertGame(game); // Attempting to insert the same game again
            fail("Expected an exception when inserting a duplicate game ID.");
        } catch (DataAccessException e) {
            assertTrue(e.getMessage().contains("Error inserting game"));
        }
    }

    @Test
    @DisplayName("Get Game - Positive Test")
    void getGamePositiveTest() {
        GameData game = new GameData(3, "whitePlayer", "blackPlayer", "game3", new ChessGame());
        try {
            gameDAO.insertGame(game);

            GameData retrievedGame = gameDAO.getGame(3);
            assertNotNull(retrievedGame);
            assertEquals(3, retrievedGame.gameID());
            assertEquals("whitePlayer", retrievedGame.whiteUsername());
            assertEquals("blackPlayer", retrievedGame.blackUsername());
            assertEquals("game3", retrievedGame.gameName());
        } catch (DataAccessException e) {
            fail("Get game failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Get Game - Negative Test (Non-existent Game)")
    void getGameNegativeTest() {
        try {
            gameDAO.getGame(999);
            fail("Expected an exception when retrieving a non-existent game.");
        } catch (DataAccessException e) {
            assertTrue(e.getMessage().contains("Game with this ID doesn't exist"));
        }
    }

    @Test
    @DisplayName("Delete Game - Positive Test")
    void deleteGamePositiveTest() {
        GameData game = new GameData(4, "whitePlayer", "blackPlayer", "game4", new ChessGame());
        try {
            gameDAO.insertGame(game);
            gameDAO.deleteGame(4);

            assertThrows(DataAccessException.class, () -> gameDAO.getGame(4));
        } catch (DataAccessException e) {
            fail("Delete game failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Delete Game - Negative Test (Non-existent Game)")
    void deleteGameNegativeTest() {
        try {
            gameDAO.deleteGame(999); // Attempting to delete a game that doesn't exist
        } catch (DataAccessException e) {
            fail("Delete game should not throw an exception for a non-existent game.");
        }
    }

    @Test
    @DisplayName("Get Games - Positive Test")
    void getGamesPositiveTest() {
        GameData game1 = new GameData(5, "whitePlayer1", "blackPlayer1", "game5", new ChessGame());
        GameData game2 = new GameData(6, "whitePlayer2", "blackPlayer2", "game6", new ChessGame());

        try {
            gameDAO.insertGame(game1);
            gameDAO.insertGame(game2);

            List<GameListData> games = gameDAO.getGames();
            assertNotNull(games);
            assertEquals(2, games.size());
        } catch (DataAccessException e) {
            fail("Get games failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Get Games - Negative Test (No Games in Database)")
    void getGamesNegativeTest() {
        try {
            List<GameListData> games = gameDAO.getGames();
            assertNotNull(games, "Game list should not be null");
            assertTrue(games.isEmpty(), "Game list should be empty when no games exist");
        } catch (DataAccessException e) {
            fail("Get games should not throw an exception when no games exist: " + e.getMessage());
        }
    }


    @Test
    @DisplayName("Clear Games - Positive Test")
    void clearGamesPositiveTest() {
        GameData game = new GameData(7, "whitePlayer", "blackPlayer", "game7", new ChessGame());
        try {
            gameDAO.insertGame(game);
            gameDAO.clear();

            assertThrows(DataAccessException.class, () -> gameDAO.getGame(7));
        } catch (DataAccessException e) {
            fail("Clear games failed: " + e.getMessage());
        }
    }
}
