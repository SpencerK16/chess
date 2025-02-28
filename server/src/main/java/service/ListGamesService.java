package service;

import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import results.ListGamesResult;
import request.ListGamesRequest;

import java.util.List;

public class ListGamesService {

    private final ListGamesRequest request;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    // Constructor to initialize the request and DAOs
    public ListGamesService(ListGamesRequest request, AuthDAO authDAO, GameDAO gameDAO) {
        this.request = request;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListGamesResult listGames() {
        try {
            String username = authDAO.getUser(request.authToken());

            if (username == null) {
                return new ListGamesResult(false, null, "Error: Unauthorized (Invalid authToken)");
            }

            List<String> gameIDs = gameDAO.getUserGames(username); // This method should return a list of game IDs for the user

            if (gameIDs.isEmpty()) {
                return new ListGamesResult(false, null, null, null, null, "No games found for the user");
            }

            // Step 3: Loop through the games and get the necessary details (e.g., gameID, whiteUsername, blackUsername, gameName)
            // This will depend on your game database schema.
            String gameID = gameIDs.get(0); // Just using the first game as an example; adjust as needed
            String whiteUsername = "user1"; // Get this from your database
            String blackUsername = "user2"; // Get this from your database
            String gameName = "Game1"; // Get this from your database

            // Step 4: Return a successful result with the games information
            return new ListGamesResult(true, gameID, whiteUsername, blackUsername, gameName, "Games retrieved successfully");

        } catch (Exception e) {
            // If any exception occurs, return a failure result with the error message
            return new ListGamesResult(false, null, null, null, null, "Error: " + e.getMessage());
        }
    }
}