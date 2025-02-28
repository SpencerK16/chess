package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import request.CreateGameRequest;
import results.CreateGameResult;
import service.CreateGameService;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CreateGameHandler {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public CreateGameHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void processRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (!method.equals("POST")) {
            sendResponse(exchange, new CreateGameResult(false, null, "Error: Only POST requests are allowed"));
            return;
        }

        InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(reader, JsonObject.class);

        String authToken = json.get("authToken").getAsString();
        String gameName = json.get("gameName").getAsString();

        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);
        CreateGameService createGameService = new CreateGameService(createGameRequest, userDAO, authDAO, gameDAO);

        CreateGameResult result = createGameService.createGame();

        sendResponse(exchange, result);
    }

    private void sendResponse(HttpExchange exchange, CreateGameResult result) throws IOException {
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(result);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        int responseCode = result.success() ? 200 : 400;
        exchange.sendResponseHeaders(responseCode, jsonResponse.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes());
        }
    }
}