package results;

public record RegisterResult(Boolean success, String username, String authToken, String message) {
}
