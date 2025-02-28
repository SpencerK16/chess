package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import results.LoginResult;
import request.LoginRequest;
import service.LoginService;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class LoginHandler {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginHandler(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public void processRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (!method.equals("POST")) {
            sendErrorResponse(exchange, 405, "Error: Only POST requests are allowed");
            return;
        }

        InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(reader, JsonObject.class);

        String username = json.get("username").getAsString();
        String password = json.get("password").getAsString();

        LoginRequest loginRequest = new LoginRequest(username, password);

        LoginService loginService = new LoginService(loginRequest, userDAO, authDAO);
        LoginResult result = loginService.login();

        if (result.success()) {
            sendSuccessResponse(exchange, result);
        } else {
            sendErrorResponse(exchange, 401, result.message());
        }
    }

    private void sendSuccessResponse(HttpExchange exchange, LoginResult result) throws IOException {
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
        JsonObject errorResponse = new JsonObject();
        errorResponse.addProperty("message", message);

        String jsonResponse = gson.toJson(errorResponse);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, jsonResponse.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes());
        }
    }
}
