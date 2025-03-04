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

            // Step 4: Return a successful result with the games information
            return new ListGamesResult(true,  gameDAO.getGames(), "Games retrieved successfully");

        } catch (Exception e) {
            // If any exception occurs, return a failure result with the error message
            return new ListGamesResult(false, null, "Error: " + e.getMessage());
        }
    }
}