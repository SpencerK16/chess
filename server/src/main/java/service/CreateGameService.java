package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;
import model.AuthData;
import request.CreateGameRequest;
import results.CreateGameResult;

import java.util.Random;

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

            int gameID = new Random().nextInt(1000);

            GameData newGame = new GameData(gameID, null, null,  request.gameName(), new ChessGame());

            gameDAO.insertGame(newGame);

            return new CreateGameResult(true, Integer.toString(gameID), "Game created successfully");

        } catch (Exception e) {
            return new CreateGameResult(false, null, "Error: " + e.getMessage());
        }
    }
}