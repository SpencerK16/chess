package handlers;

import com.google.gson.Gson;
import request.CreateGameRequest;
import results.CreateGameResult;
import service.CreateGameService;
import dataaccess.AuthDAO;
import spark.Request;
import spark.Response;
import java.io.IOException;
import java.util.Map;

public class CreateGameHandler {

    private final AuthDAO authDAO;
    private static CreateGameService createGameService = null;

    public CreateGameHandler(AuthDAO authDAO, CreateGameService createGameService) {
        this.authDAO = authDAO;
        this.createGameService = createGameService;
    }

    public static Object processRequest(Request req, Response res) throws IOException {
        try {
            String authToken = req.headers("authorization");

            if (authToken == null || authToken.isEmpty()) {
                res.status(401);
                res.body("{ \"message\": \"Error: unauthorized\" } ");
                return res.body();
            }

            Gson gson = new Gson();
            CreateGameRequest createGameRequest = new CreateGameRequest(authToken, req.queryParams("gameName"));

            if (createGameRequest.gameName() == null || createGameRequest.gameName().isEmpty()) {
                res.status(400);
                res.body("{ \"message\": \"Error: bad request\" } ");
                return res.body();
            }

            CreateGameResult result = createGameService.createGame();

            if (result.success()) {
                res.status(200);
                res.body(gson.toJson(result));
            } else {
                res.status(500);
                res.body(gson.toJson(Map.of("message", "Error: " + result.message())));
            }

    } catch (Exception e) {
        e.printStackTrace(); // Log the full stack trace to help debug
        res.status(500);
        return new Gson().toJson(Map.of("message", "Internal server error", "error", e.getMessage()));
    }
        return res.body();
    }
}
