package handlers;

import chess.ChessBoard;
import chess.ChessBoardAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ChessBoard.class, new ChessBoardAdapter());
        Gson gson = gsonBuilder.create();
        String authToken = req.headers("authorization");

        ListGamesRequest request = new ListGamesRequest(authToken);
        ListGamesResult result = new ListGamesService(request, new AuthDAO(), new GameDAO()).listGames();

        if (result.success()) {
            res.status(200);
            return "{ \"success\":true, \"games\": " + gson.toJson(result.games()) + ", \"message\": \"okay\"}";
        } else {
            res.status(401);
            return "{ \"message\": \"Error: " + result.message().replace("\"", "\\\"") + "\" }";
        }
    }
}
