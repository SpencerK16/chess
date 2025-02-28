package results;

public record LoginResult(Boolean success, String username, String authToken, String message) {
}
