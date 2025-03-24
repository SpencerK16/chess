package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import request.JoinGameRequest;
import results.JoinGameResult;
import service.JoinGameService;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import spark.Request;
import spark.Response;
import java.io.IOException;
import java.util.Objects;

public class JoinGameHandler {
    public static Object processRequest(Request req, Response res) throws IOException {
        String authToken = req.headers("authorization");
    try {
        if (authToken == null || authToken.isEmpty()) {
            res.status(401);
            res.body("{ \"message\": \"Error: unauthorized\" } ");
            return res.body();
        }
        new AuthDAO().getAuth(authToken);
    } catch (DataAccessException d) {
        res.status(401);
        res.body("{ \"message\": \"Error: unauthorized\" } ");
        return res.body();
    }

        Gson gson = new Gson();
        JoinGameRequest iRequest = gson.fromJson(req.body(), JoinGameRequest.class);
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, iRequest.playerColor(), iRequest.gameID());
        if (joinGameRequest.playerColor() == null || joinGameRequest.gameID() == null || joinGameRequest.playerColor().isEmpty()) {
            res.status(400);
            res.body("{ \"message\": \"Error: bad request\" } ");
            return res.body();
        }

        if(!(joinGameRequest.playerColor().equalsIgnoreCase("BLACK") || joinGameRequest.playerColor().equalsIgnoreCase("WHITE"))) {
            res.status(400);
            res.body("{ \"message\": \"Error: bad request\" } ");
            return res.body();
        }

        JoinGameService joinGameService = new JoinGameService(joinGameRequest, new UserDAO(), new AuthDAO(), new GameDAO());
        JoinGameResult result = joinGameService.joinGame();

        if (result.success()) {
            res.status(200);
            res.body("{}");
        } else {
            if (result.message().equals("Error: Error: unauthorized")) {
                res.status(401);
                return "{ \"message\": \"Error: unauthorized\" }";
            } else if (result.message().equals("Error: Player color already taken or invalid.")) {
                res.status(403);
                return "{ \"message\": \"Error: already taken\" }";
            } else {
                res.status(500);
                return "{ \"message\": \"Error: " + result.message().replace("\"", "\\\"") + "\" }";
            }
        }

        return res.body();
    }
}
