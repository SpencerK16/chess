package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthDAO {

    public AuthDAO() {
    }

    public void insertAuth(AuthData authData) throws DataAccessException {
        String sql = "INSERT INTO tokens (authToken, username) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authData.authToken());
            stmt.setString(2, authData.username());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting auth token: " + e.getMessage());
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        String sql = "SELECT * FROM tokens WHERE authToken = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new AuthData(
                            rs.getString("username"),
                            rs.getString("authToken")
                    );
                } else {
                    throw new DataAccessException("Auth token doesn't exist.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving auth token: " + e.getMessage());
        }
    }

    public String getUser(String authToken) throws DataAccessException {
        String sql = "SELECT username FROM tokens WHERE authToken = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                } else {
                    throw new DataAccessException("Auth token doesn't exist.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving username: " + e.getMessage());
        }
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        String sql = "DELETE FROM tokens WHERE authToken = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting auth token: " + e.getMessage());
        }
    }

    public void clear() throws DataAccessException {
        String sql = "DELETE FROM tokens";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing auth tokens: " + e.getMessage());
        }
    }
}