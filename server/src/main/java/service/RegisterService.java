package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import results.RegisterResult;
import java.util.UUID;

public class RegisterService {
    private final RegisterRequest request;
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterService(RegisterRequest request) {
        this.request = request;
        this.userDAO = new UserDAO();
        this.authDAO = new AuthDAO();
        // Initialize DAOs
        // Store request
    }

    public RegisterResult register() {
        try {
            if(userDAO.usernameExists(request.username())) {
                return new RegisterResult(false, null, null, "Error: Username already taken");
            }
            if (request.password().isEmpty()) {
                return new RegisterResult(false, null, null, "Error: Password too short");
            }



            UserData newUser = new UserData(request.username(), request.password(), request.email());

            userDAO.insertUser(newUser); // Store the user in the database

            String authToken = UUID.randomUUID().toString();


            AuthData authData = new AuthData(request.username(), authToken);
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