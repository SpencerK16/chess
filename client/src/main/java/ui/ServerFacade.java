package ui;

import chess.ChessBoard;
import chess.ChessBoardAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import request.*;
import results.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest request) {
        var path = "/user";

        try {
            RegisterResult res = this.makeRequest("POST", path, request, RegisterResult.class, null);
            if(res.success() == null) {
                return new RegisterResult(true, res.username(), res.authToken(), res.message());
            }
            return res;
        } catch (FacadeException f) {
            return new RegisterResult(false, "", "", f.message);
        }
    }

    public LogoutResult logout(LogoutRequest request) {
        var path = "/session";
        try {
            LogoutResult res = this.makeRequest("DELETE", path, request, LogoutResult.class, request.authToken());
            if(res.success() == null) {
                return new LogoutResult(true, res.message());
            }

            return res;
        } catch(FacadeException f) {
            return new LogoutResult(false, f.message);
        }
    }

    public LoginResult login(LoginRequest request){
        var path = "/session";
        try {
            LoginResult res = this.makeRequest("POST", path, request, LoginResult.class, null);
            if(res.success() == null) {
                return new LoginResult(true, res.username(), res.authToken(), res.message());
            }
            return res;
        } catch(FacadeException f) {
            return new LoginResult(false, "", "", f.message);
        }
    }

    public ListGamesResult listGames(ListGamesRequest request) {
        var path = "/game";
        try {
            return this.makeRequest("GET", path, request, ListGamesResult.class, request.authToken());
        } catch(FacadeException f) {
            return new ListGamesResult(false, null, f.message);
        }
    }

    public JoinGameResult joinGame(JoinGameRequest request) {
        var path = "/game";
        try {
            JoinGameResult res = this.makeRequest("PUT", path, request, JoinGameResult.class, request.authToken());

            if(res.success() == null) {
                return new JoinGameResult(true, res.message());
            }

            return res;
        } catch(FacadeException f) {
            return new JoinGameResult(false, f.message);
        }
    }

    public CreateGameResult createGame(CreateGameRequest request) {
        var path = "/game";
        try {
            return this.makeRequest("POST", path, request, CreateGameResult.class, request.authToken());
        } catch(FacadeException f) {
            return new CreateGameResult(false, "", f.message);
        }
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String auth) throws FacadeException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if(auth != null) {
                http.setRequestProperty("authorization", auth);

            }
            //
            if (method.equals("GET") == false)
            {
                writeBody(request, http);
            }

            http.connect();

            if(!isSuccessful(http.getResponseCode())) {
                throw new FacadeException(http.getResponseMessage());
            }

            return readBody(http, responseClass);
        } catch (IOException ex) {
            throw new FacadeException(ex.getMessage());
        }
        catch (URISyntaxException u) {
            throw new FacadeException("Good luck.");
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        try (InputStream respBody = http.getInputStream()) {
            if (respBody != null && responseClass != null) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(ChessBoard.class, new ChessBoardAdapter());
                Gson gson = gsonBuilder.create();
                InputStreamReader reader = new InputStreamReader(respBody);
                response = gson.fromJson(reader, responseClass);
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
