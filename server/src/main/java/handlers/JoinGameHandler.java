package handlers;

import com.google.gson.Gson;
import request.JoinGameRequest;
import results.JoinGameResult;
import service.JoinGameService;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import spark.Request;
import spark.Response;
import java.io.IOException;

public class JoinGameHandler {

    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private static UserDAO userDAO;

    public JoinGameHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public static Object processRequest(Request req, Response res) throws IOException {
        String authToken = req.headers("authtoken");

        if (authToken == null || authToken.isEmpty()) {
            res.status(401);
            res.body("{ \"message\": \"Error: unauthorized\" } ");
            return "";
        }

        Gson gson = new Gson();
        JoinGameRequest joinGameRequest = gson.fromJson(req.body(), JoinGameRequest.class);

        if (joinGameRequest.playerColor() == null || joinGameRequest.gameID() == null) {
            res.status(400);
            res.body("{ \"message\": \"Error: bad request\" } ");
            return "";
        }

        // Add the authToken to the request
        joinGameRequest = new JoinGameRequest(authToken, joinGameRequest.playerColor(), joinGameRequest.gameID());

        JoinGameService joinGameService = new JoinGameService(joinGameRequest, userDAO, authDAO, gameDAO);
        JoinGameResult result = joinGameService.joinGame();

        if (result.success()) {
            res.status(200);
            res.body("{}");
        } else {
            if (result.message().equals("Error: unauthorized")) {
                res.status(401);
                res.body("{ \"message\": \"Error: unauthorized\" } ");
            } else if (result.message().equals("Error: Player color already taken or invalid.")) {
                res.status(403);
                res.body("{ \"message\": \"Error: already taken\" } ");
            } else {
                res.status(500);
                res.body("{ \"message\": \"Error: " + result.message().replace("\"", "\\\"") + "\" }");
            }
        }

        return res.body();
    }
}
