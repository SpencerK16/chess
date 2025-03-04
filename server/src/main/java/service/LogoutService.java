package service;

import dataaccess.AuthDAO;
import model.AuthData;
import request.LogoutRequest;
import results.LogoutResult;

public class LogoutService {

    private final LogoutRequest request;
    private final AuthDAO authDAO;

    // Constructor to initialize the request and DAO
    public LogoutService(LogoutRequest request, AuthDAO authDAO) {
        this.request = request;
        this.authDAO = authDAO;
    }

    // Method to log out the user by removing the authToken
    public LogoutResult logout() {
        try {
            // Step 1: Check if the authToken exists in the AuthDAO
            AuthData token = authDAO.getAuth(request.authToken());

            if (token == null) {
                // If the token does not exist, return a failure result
                return new LogoutResult(false, "Error: Invalid authToken");
            }

            // Step 2: Remove the authToken from the DAO
            authDAO.deleteAuth(token.authToken());

            // Step 3: Return a success result
            return new LogoutResult(true, "Logout successful");
        } catch (Exception e) {
            // If any exception occurs, return a failure result with the error message
            return new LogoutResult(false, "Error: " + e.getMessage());
        }
    }
}