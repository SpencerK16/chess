package dataaccess;

import model.GameData;
import model.GameListData;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class SQLGameDAO {

    public SQLGameDAO() {
    }

    // Insert Game
    public void insertGame(GameData gameData) throws DataAccessException {
        String sql = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, gameState) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gameData.gameID());
            stmt.setString(2, gameData.whiteUsername());
            stmt.setString(3, gameData.blackUsername());
            stmt.setString(4, gameData.gameName());
            stmt.setString(5, gameData.gameState()); // Assuming the game state is serialized to JSON
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting game: " + e.getMessage());
        }
    }

    // Get Game by ID
    public GameData getGame(int gameID) throws DataAccessException {
    }

    public void deleteGame(int gameID) throws DataAccessException {
    }

    public void clear() throws DataAccessException {
    }

    public List<GameListData> getGames() throws DataAccessException {
    }
}