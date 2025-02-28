package service;

import dataaccess.UserDAO;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import results.ClearResult;

public class ClearService {

    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public ClearService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ClearResult clear() {
        try {
            userDAO.clear();

            gameDAO.clear();

            authDAO.clear();

            return new ClearResult(true, "Data cleared successfully.");

        } catch (Exception e) {
            return new ClearResult(false, "Error: " + e.getMessage());
        }
    }
}