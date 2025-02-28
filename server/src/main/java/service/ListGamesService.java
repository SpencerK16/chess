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

    // Method to list all games for the logged-in user
    public ListGamesResult listGames() {
        try {
            // Step 1: Check if the authToken is valid
            String username = authDAO.getAuth(request.authToken());

            if (username == null) {
                // If the authToken is invalid, return a failure result
                return new ListGamesResult(false, null, null, null, null, "Error: Unauthorized (Invalid authToken)");
            }

            // Step 2: Fetch the games associated with this user (you can adjust this logic to your database schema)
            List<String> gameIDs = gameDAO.getGamesForUser(username); // This method should return a list of game IDs for the user

            if (gameIDs.isEmpty()) {
                // If no games found, return a result indicating no games
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