package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import request.LoginRequest;
import results.LoginResult;

import java.util.UUID;

public class LoginService {

    private final LoginRequest request;
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginService(LoginRequest request) {
        this.request = request;
        this.userDAO = new UserDAO();
        this.authDAO = new AuthDAO();
    }

    public LoginService(LoginRequest request, UserDAO userDAO, AuthDAO authDAO) {
        this.request = request;
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public LoginResult login() {
        try {
            if(!userDAO.usernameExists(request.username())) {
                return new LoginResult(false, null, null, "Error: User does not exist!");
            }

            UserData existingUser = userDAO.getUser(request.username());

            if (!BCrypt.checkpw(request.password(), existingUser.password())) {
                return new LoginResult(false, null, null, "Error: Unauthorized");
            }

            String authToken = UUID.randomUUID().toString();

            AuthData authData = new AuthData(request.username(), authToken);
            authDAO.insertAuth(authData);

            return new LoginResult(true, request.username(), authToken, "Login successful");

        } catch (Exception e) {
            return new LoginResult(false, null, null, "Error: " + e.getMessage());
        }
    }

}
