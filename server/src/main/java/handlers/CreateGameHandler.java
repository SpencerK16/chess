package handlers;

import com.google.gson.Gson;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;
import model.UserData;
import request.CreateGameRequest;
import results.CreateGameResult;
import service.CreateGameService;
import dataaccess.AuthDAO;
import spark.Request;
import spark.Response;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class CreateGameHandler {

    public static Object processRequest(Request req, Response res) throws IOException {
        try {
            String authToken = req.headers("authorization");

            Gson gson = new Gson();
            GameData game = gson.fromJson(req.body(), GameData.class);

            CreateGameRequest request = new CreateGameRequest(authToken, game.gameName());

            if (authToken == null || authToken.isEmpty()) {
                res.status(401);
                res.body("{ \"message\": \"Error: unauthorized\" } ");
                return res.body();
            }

            if (request.gameName() == null || request.gameName().isEmpty()) {
                res.status(400);
                res.body("{ \"message\": \"Error: bad request\" } ");
                return res.body();
            }

            CreateGameResult result = new CreateGameService(request, new UserDAO(), new AuthDAO(), new GameDAO()).createGame();

            if (result.success()) {
                res.status(200);
                res.body(gson.toJson(result));
            }
            else if (Objects.equals(result.message(), "Error: AuthToken doesn't exist.")) {
                res.status(401);
                res.body(gson.toJson(result));
            } else {
                res.status(500);
                res.body(gson.toJson(Map.of("message", "Error: " + result.message())));
            }

    } catch (Exception e) {
        e.printStackTrace(); // Log the full stack trace to help debug
        res.status(401);
        return new Gson().toJson(Map.of("message", "Internal server error", "error", e.getMessage()));
    }
        return res.body();
    }
}
