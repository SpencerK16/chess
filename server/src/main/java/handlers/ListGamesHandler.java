package handlers;

import com.google.gson.Gson;
import dataaccess.GameDAO;
import request.ListGamesRequest;
import results.ListGamesResult;
import service.ListGamesService;
import dataaccess.AuthDAO;
import spark.Request;
import spark.Response;
import java.io.IOException;

public class ListGamesHandler {
    public static Object processRequest(Request req, Response res) throws IOException {
        Gson gson = new Gson();
        String authToken = req.headers("authorization");

        ListGamesRequest request = new ListGamesRequest(authToken);
        ListGamesResult result = new ListGamesService(request, new AuthDAO(), new GameDAO()).listGames();

        if (result.success()) {
            res.status(200);
            return "{ \"games\": " + gson.toJson(result.games()) + "}";
        } else {
            res.status(401);
            return "{ \"message\": \"Error: " + result.message().replace("\"", "\\\"") + "\" }";
        }
    }
}
