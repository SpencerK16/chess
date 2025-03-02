package handlers;

import com.google.gson.Gson;
import request.CreateGameRequest;
import results.CreateGameResult;
import service.CreateGameService;
import dataaccess.AuthDAO;
import spark.Request;
import spark.Response;
import java.io.IOException;

public class CreateGameHandler {

    private final AuthDAO authDAO;
    private static CreateGameService createGameService = null;

    public CreateGameHandler(AuthDAO authDAO, CreateGameService createGameService) {
        this.authDAO = authDAO;
        this.createGameService = createGameService;
    }

    public static Object processRequest(Request req, Response res) throws IOException {
        String authToken = req.attributes().toArray()[0].toString();

        if (authToken == null || authToken.isEmpty()) {
            res.status(401);
            res.body("{ \"message\": \"Error: unauthorized\" } ");
            return "";
        }

        Gson gson = new Gson();
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, req.queryParams("gameName"));

        if (createGameRequest.gameName() == null || createGameRequest.gameName().isEmpty()) {
            res.status(400);
            res.body("{ \"message\": \"Error: bad request\" } ");
            return "";
        }

        CreateGameResult result = createGameService.createGame();

        if (result.success()) {
            res.status(200);
            res.body(gson.toJson(result));
        } else {
            res.status(500);
            StringBuilder sb = new StringBuilder();
            sb.append("{ \"message\": \"Error: ");
            sb.append(result.message());
            sb.append("\" } ");
            res.body(sb.toString());
        }

        return "";
    }
}
