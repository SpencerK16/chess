package handlers;

import com.google.gson.Gson;
import results.ListGamesResult;
import service.ListGamesService;
import dataaccess.AuthDAO;
import spark.Request;
import spark.Response;
import java.io.IOException;

public class ListGamesHandler {

    private final AuthDAO authDAO;
    private static ListGamesService listGamesService = null;

    public ListGamesHandler(AuthDAO authDAO, ListGamesService listGamesService) {
        this.authDAO = authDAO;
        this.listGamesService = listGamesService;
    }

    public static Object processRequest(Request req, Response res) throws IOException {
        Gson gson = new Gson();
        String authToken = req.attributes().toArray()[0].toString();

        ListGamesResult result = listGamesService.listGames();

        if (result.success()) {
            res.status(200);
            res.body("{ \"games\": " + gson.toJson(result.games()) + "}");
        } else {
            res.status(500);
            return "{ \"message\": \"Error: " + result.message().replace("\"", "\\\"") + "\" }";
        }

        return res.body();
    }
}
