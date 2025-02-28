package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import results.RegisterResult;
import java.util.UUID;

public class RegisterService {
    private RegisterRequest request;
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public RegisterService(RegisterRequest request, UserDAO userDAO, AuthDAO authDAO) {
        this.request = request;
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        // Initialize DAOs
        // Store request
    }

    public RegisterResult register() {
        try {
            UserData existingUser = userDAO.getUser(request.username());
            if (existingUser != null) {
                return new RegisterResult(false, null, null, "Error: Username already taken");
            }

            if (request.password().length() < 6) {
                return new RegisterResult(false, null, null, "Error: Password too short");
            }

            UserData newUser = new UserData(request.username(), request.password(), request.email());

            userDAO.insertUser(newUser); // Store the user in the database

            String authToken = UUID.randomUUID().toString();


            AuthData authData = new AuthData(authToken, request.username());
            authDAO.insertAuth(authData);

            return new RegisterResult(true, request.username(), authToken, "Registration successful");

        } catch (Exception e) {
            return new RegisterResult(false, null, null, "Error: " + e.getMessage());
        }
    }
    /*  Validate Request
     *       Check if user exists
     *       Check if password is valid
     *   Create the user (UserData)
     *   Store UserData
     *   Create AuthToken
     *   Store AuthToken
     *   Make Result
     *   Return Result
     *
     *   Catch exception and pass it to the handler in the result
     * */

}