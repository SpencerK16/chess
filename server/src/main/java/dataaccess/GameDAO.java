package dataaccess;

import chess.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.GameData;
import model.GameListData;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameDAO {

    public GameDAO() {
    }

    public void insertGame(GameData gameData) throws DataAccessException {
        String sql = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(ChessBoard.class, new ChessBoardAdapter());
            Gson gson = gsonBuilder.create();
            String json_of_games_with_adapter = gson.toJson(gameData.game());
            //ChessGame game_from_json_string = new Gson().fromJson(game_as_json_string, ChessGame.class);
            stmt.setInt(1, gameData.gameID());
            stmt.setString(2, gameData.whiteUsername());
            stmt.setString(3, gameData.blackUsername());
            stmt.setString(4, gameData.gameName());
            stmt.setString(5, json_of_games_with_adapter);
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
                //System.out.println(rs.getString("game"));
                    String game_as_json = rs.getString("game");
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(ChessBoard.class, new ChessBoardAdapter());
                    Gson gson = gsonBuilder.create();
                    return new GameData(
                            rs.getInt("gameID"),
                            rs.getString("whiteUsername"),
                            rs.getString("blackUsername"),
                            rs.getString("gameName"),
                            gson.fromJson(rs.getString("game"), ChessGame.class)
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