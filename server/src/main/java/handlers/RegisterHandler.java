package handlers;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import results.RegisterResult;
import request.RegisterRequest;
import service.RegisterService;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import spark.Request;
import spark.Response;
import java.io.IOException;


public class RegisterHandler {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;


    public RegisterHandler(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public static Object processRequest(Request req, Response res) throws IOException {
        Gson gson = new Gson();

        UserData newUser = gson.fromJson(req.body(), UserData.class);

        RegisterRequest registerRequest = new RegisterRequest(newUser.username(), newUser.password(), newUser.email());

        RegisterService registerService = new RegisterService(registerRequest);

        RegisterResult result = registerService.register();

        if(result.success()) {
            res.status(200);
            res.body(gson.toJson(new AuthData(result.username(), result.authToken())));
        } else {
            if(result.message() == "Error: Password too short" || result.message().contains("null")) {
                res.status(400);
                res.body("{ \"message\": \"Error: bad request\" } ");
            }
            else if(result.message() == "Error: Username already taken") {
                res.status(403);
                res.body("{ \"message\": \"Error: already taken\" } ");
            }
            else {
                res.status(500);
                res.body("{ \"message\": \"Error: " + result.message().replace("\"", "\\\"") + "\" }");
            }
        }

        return res.body();
    }
}
