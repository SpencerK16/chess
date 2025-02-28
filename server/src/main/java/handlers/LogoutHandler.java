package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import results.LogoutResult;
import request.LogoutRequest;
import service.LogoutService;
import dataaccess.AuthDAO;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class LogoutHandler {

    private final AuthDAO authDAO;

    // Constructor to initialize the AuthDAO
    public LogoutHandler(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    // Process the logout request
    public void processRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        // Validate that the request method is POST
        if (!method.equals("POST")) {
            sendErrorResponse(exchange, 405, "Error: Only POST requests are allowed");
            return;
        }

        // Parse the request body into a LogoutRequest object
        InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        LogoutRequest logoutRequest = gson.fromJson(reader, LogoutRequest.class);

        // Call the LogoutService
        LogoutService logoutService = new LogoutService(logoutRequest, authDAO);
        LogoutResult result = logoutService.logout();

        // Send the response based on the result
        if (result.success()) {
            sendSuccessResponse(exchange, result);
        } else {
            sendErrorResponse(exchange, 401, result.message());
        }
    }

    // Send a successful response with the LogoutResult in JSON format
    private void sendSuccessResponse(HttpExchange exchange, LogoutResult result) throws IOException {
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(result);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes());
        }
    }

    // Send an error response with the given status code and message
    private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(new LogoutResult(false, message));

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, jsonResponse.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes());
        }
    }
}