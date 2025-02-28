package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import results.LoginResult;

import java.util.UUID;

public class LoginService {

    private LoginRequest request;
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public LoginService(LoginRequest request, UserDAO userDAO, AuthDAO authDAO) {
        this.request = request;
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }


    public LoginResult login() {
        try {
            UserData existingUser = userDAO.getUser(request.username());
            if (existingUser == null) {
                return new LoginResult(false, null, null, "Error: Unauthorized");
            }

            if (!existingUser.password().equals(request.password())) {
                return new LoginResult(false, null, null, "Error: Unauthorized");
            }
            String authToken = UUID.randomUUID().toString();

            AuthData authData = new AuthData(authToken, request.username());
            authDAO.insertAuth(authData);

            return new LoginResult(true, request.username(), authToken, "Login successful");

        } catch (Exception e) {
            return new LoginResult(false, null, null, "Error: " + e.getMessage());
        }
    }

}



/*
    Find the user in the userDAO
    Make a new authotken if they exist
    Store the authtoken in the DAO
    Return the authtoken in the result

 */