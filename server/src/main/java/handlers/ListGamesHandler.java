package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import results.ListGamesResult;
import request.ListGamesRequest;
import service.ListGamesService;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ListGamesHandler {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public ListGamesHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    // String authToken = req.attributes().toArray()[0];
    public void processRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (!method.equals("POST")) {
            sendErrorResponse(exchange, 405, "Error: Only POST requests are allowed");
            return;
        }

        InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        ListGamesRequest listGamesRequest = gson.fromJson(reader, ListGamesRequest.class);

        ListGamesService listGamesService = new ListGamesService(listGamesRequest, authDAO, gameDAO);
        ListGamesResult result = listGamesService.listGames();

        if (result.success()) {
            sendSuccessResponse(exchange, result);
        } else {
            sendErrorResponse(exchange, 401, result.message());
        }
    }

    private void sendSuccessResponse(HttpExchange exchange, ListGamesResult result) throws IOException {
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(result);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes());
        }
    }

    private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(new ListGamesResult(false, null, message));

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, jsonResponse.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes());
        }
    }
}