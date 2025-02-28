package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import results.ClearResult;
import service.ClearService;

import java.io.IOException;
import java.io.OutputStream;

public class ClearHandler {

    private final ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public void processRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (!method.equals("POST")) {
            sendResponse(exchange, new ClearResult(false, "Error: Only POST requests are allowed"));
            return;
        }

        ClearResult result = clearService.clear();

        sendResponse(exchange, result);
    }

    private void sendResponse(HttpExchange exchange, ClearResult result) throws IOException {
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(result);

        exchange.getResponseHeaders().set("Content-Type", "application/json");

        exchange.sendResponseHeaders(result.success() ? 200 : 400, jsonResponse.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes());
        }
    }
}