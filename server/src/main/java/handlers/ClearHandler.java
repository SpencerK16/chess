package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import org.eclipse.jetty.server.Authentication;
import results.ClearResult;
import service.ClearService;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.OutputStream;

public class ClearHandler {
    public ClearHandler() {}

    public static Object processRequest(Request req, Response res) {
        ClearResult result = new ClearService(new UserDAO(), new GameDAO(), new AuthDAO()).clear();

        if(result.success()) {
            res.status(200);
        } else {
            res.status(500);
            res.body(new Gson().toJson(result.message()));
        }

        return "";
    }
}