package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import results.RegisterResult;
import request.RegisterRequest;
import service.RegisterService;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RegisterHandler {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;


    public RegisterHandler(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult processRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (!method.equals("POST")) {
            return new RegisterResult(false, null, null, "Error: Only POST requests are allowed");
        }

        InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Gson gson = new Gson();

        JsonObject json = gson.fromJson(reader, JsonObject.class);

        String username = json.get("username").getAsString();
        String password = json.get("password").getAsString();
        String email = json.get("email").getAsString();

        RegisterRequest registerRequest = new RegisterRequest(username, password, email);

        RegisterService registerService = new RegisterService(registerRequest);

        RegisterResult result = registerService.register();

        sendResponse(exchange, result);

        return result;
    }

    private void sendResponse(HttpExchange exchange, RegisterResult result) throws IOException {
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(result);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes());
        }
    }
}


        /*
            Get the request method (post/get)
            Validate that it is POST
            Get the request
            Use the given code to parse the request
            Initialize a Gson instance
            Use Gson to read the data into a request
            make a new service (put the request into the service)
            call the service.register()
            Return the result
         */