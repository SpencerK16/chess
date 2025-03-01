package handlers;

import dataaccess.AuthDAO;
import request.LogoutRequest;
import results.LogoutResult;
import service.LogoutService;
import spark.Request;
import spark.Response;
import java.io.IOException;

public class LogoutHandler {

    private static final AuthDAO AUTH_DAO;

    static {
        AUTH_DAO = new AuthDAO();
    }



    public static Object processRequest(Request req, Response res) throws IOException {
        String authToken = req.headers("authorization");

        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        LogoutService logoutService = new LogoutService(logoutRequest, AUTH_DAO);
        LogoutResult result = logoutService.logout();

        if (authToken == null || authToken.isEmpty()) {
            res.status(401);
            res.body("{ \"message\": \"Error: unauthorized\" } ");
            return "";
        }

        if (result.success()) {
            res.status(200);
            res.body("{}");
        } else {
            if (result.message().equals("Error: unauthorized")) {
                res.status(401);
                res.body("{ \"message\": \"Error: unauthorized\" } ");
            } else {
                res.status(500);
                res.body("{ \"message\": \"Error: " + result.message().replace("\"", "\\\"") + "\" }");
            }
        }

        return res.body();
    }
}
