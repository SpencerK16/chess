package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.GameListData;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class GameDAO {

    public GameDAO() {
    }

    public void insertGame(GameData gameData) throws DataAccessException {
        String sql = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gameData.gameID());
            stmt.setString(2, gameData.whiteUsername());
            stmt.setString(3, gameData.blackUsername());
            stmt.setString(4, gameData.gameName());
            stmt.setString(5, new Gson().toJson(gameData.game()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting game: " + e.getMessage());
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        String sql = "SELECT * FROM games WHERE gameID = ?";

        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gameID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                System.out.println(rs.getString("game"));
                    return new GameData(
                            rs.getInt("gameID"),
                            rs.getString("whiteUsername"),
                            rs.getString("blackUsername"),
                            rs.getString("gameName"),
                            new Gson().fromJson(rs.getString("game"), ChessGame.class)
                    );
                } else {
                    throw new DataAccessException("Game with this ID doesn't exist.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error getting game: " + e.getMessage());
        }
    }

    public void deleteGame(int gameID) throws DataAccessException {
        String sql = "DELETE FROM games WHERE gameID = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, gameID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting game: " + e.getMessage());
        }
    }

    public void clear() throws DataAccessException {
        String sql = "DELETE FROM games";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing games: " + e.getMessage());
        }
    }

    public List<GameListData> getGames() throws DataAccessException {
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName FROM games";
        List<GameListData> toReturn = new LinkedList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                toReturn.add(new GameListData(
                        rs.getInt("gameID"),
                        rs.getString("whiteUsername"),
                        rs.getString("blackUsername"),
                        rs.getString("gameName")
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error getting games: " + e.getMessage());
        }

        return toReturn;
    }
}