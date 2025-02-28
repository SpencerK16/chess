package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import results.JoinGameResult;
import request.JoinGameRequest;
import service.JoinGameService;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class JoinGameHandler {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public JoinGameHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void processRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (!method.equals("POST")) {
            sendResponse(exchange, new JoinGameResult(false, "Error: Only POST requests are allowed"));
            return;
        }

        InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(reader, JsonObject.class);

        String authToken = json.get("authToken").getAsString();
        String playerColor = json.get("playerColor").getAsString();
        String gameID = json.get("gameID").getAsString();

        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, playerColor, gameID);

        JoinGameService joinGameService = new JoinGameService(joinGameRequest, userDAO, authDAO, gameDAO);

        JoinGameResult result = joinGameService.joinGame();

        sendResponse(exchange, result);
    }

    private void sendResponse(HttpExchange exchange, JoinGameResult result) throws IOException {
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(result);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(result.success() ? 200 : 400, jsonResponse.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes());
        }
    }
}