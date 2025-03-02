package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;
import results.JoinGameResult;
import request.JoinGameRequest;

import java.util.Objects;

public class JoinGameService {

    private final JoinGameRequest request;
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public JoinGameService(JoinGameRequest request, UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.request = request;
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public JoinGameResult joinGame() {
        try {
            String username = validateAuthToken(request.authToken());

            GameData game = gameDAO.getGame(Integer.parseInt(request.gameID()));

            boolean success = addPlayerToGame(game, username, request.playerColor());

            if (success) {
                return new JoinGameResult(true, "Successfully joined the game.");
            } else {
                return new JoinGameResult(false, "Error: Player color already taken or invalid.");
            }

        } catch (Exception e) {
            return new JoinGameResult(false, "Error: " + e.getMessage());
        }
    }

    private String validateAuthToken(String authToken) throws Exception {
        String user = authDAO.getUser(authToken);
        if(!Objects.equals(user, "")) return user;
        throw new Exception("Error: unauthorized");
    }

    private boolean addPlayerToGame(GameData game, String username, String playerColor) {
        if (playerColor.equalsIgnoreCase("white") && game.whiteUsername() == null) {
            game = game.withWhiteUsername(username);
            return true;
        } else if (playerColor.equalsIgnoreCase("black") && game.blackUsername() == null) {
            game = game.withWhiteUsername(username);
            return true;
        }
        return false;
    }
}