package results;

public record ListGamesResult(Boolean success, String gameID, String whiteUsername, String blackUsername, String gameName, String message) {
}
