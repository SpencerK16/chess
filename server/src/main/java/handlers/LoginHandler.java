package handlers;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import results.LoginResult;
import request.LoginRequest;
import service.LoginService;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import spark.Request;
import spark.Response;
import java.io.IOException;
public class LoginHandler {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginHandler(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public static Object processRequest(Request req, Response res) throws IOException {
        Gson gson = new Gson();

        UserData user = gson.fromJson(req.body(), UserData.class);

        LoginRequest loginRequest = new LoginRequest(user.username(), user.password());

        LoginService loginService = new LoginService(loginRequest);

        LoginResult result = loginService.login();

        if (result.success()) {
            res.status(200);
            res.body(gson.toJson(new AuthData(result.username(), result.authToken())));
        } else {
            if (result.message()=="Error: Unauthorized") {
                res.status(401);
                res.body("{ \"message\": \"Error: unauthorized\" } ");
            } else {
                res.status(500);
                StringBuilder sb = new StringBuilder();
                sb.append("{ \"message\": \"Error: ");
                sb.append(result.message());
                sb.append("\" } ");
                res.body(sb.toString());
            }
        }

        return "";
    }

}
