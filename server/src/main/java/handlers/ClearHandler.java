package handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import results.ClearResult;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {
    public ClearHandler() {}

    public static Object processRequest(Request req, Response res) {
        ClearResult result = new ClearService(new UserDAO(), new GameDAO(), new AuthDAO()).clear();

        if(result.success()) {
            res.status(200);
            res.body("");
        } else {
            res.status(401);
            res.body(new Gson().toJson(result.message()));
        }

        return res.body();
    }
}