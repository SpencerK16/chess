package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;
import model.AuthData;
import request.CreateGameRequest;
import results.CreateGameResult;
import java.util.UUID;

public class CreateGameService {

    private final CreateGameRequest request;
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public CreateGameService(CreateGameRequest request, UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.request = request;
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateGameResult createGame() {
        try {
            AuthData authData = authDAO.getAuth(request.authToken());

            if (authData == null) {
                return new CreateGameResult(false, null, "Error: Unauthorized");
            }
            String gameID = UUID.randomUUID().toString();
            GameData newGame = new GameData(gameID, request.gameName(), authData.username(), null); // White user is the one creating the game

            gameDAO.insertGame(newGame);

            return new CreateGameResult(true, gameID, "Game created successfully");

        } catch (Exception e) {
            return new CreateGameResult(false, null, "Error: " + e.getMessage());
        }
    }
}