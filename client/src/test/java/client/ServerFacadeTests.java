package client;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import request.*;
import results.*;
import server.Server;
import ui.ChessClient;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static ChessClient client;

    @BeforeAll
    public static void init() {
        server = new Server();
        serverFacade = new ServerFacade("http://localhost:8080");
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @BeforeEach
    void cleanupBeforeEach() {
        try {
            new UserDAO().clear();
            new AuthDAO().clear();
            new GameDAO().clear();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cleanup before test failed.", e);
        }
    }

    @AfterEach
    void cleanupAfterEach() {
        try {
            new UserDAO().clear();
            new AuthDAO().clear();
            new GameDAO().clear();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cleanup after test failed.", e);
        }
    }


    @Test
    public void testRegisterSuccess() throws ResponseException {
        RegisterRequest request = new RegisterRequest("testUser", "password", "email@example.com");
        RegisterResult result = serverFacade.register(request);
        assertTrue(result.success());
        assertEquals("testUser", result.username());
    }

    @Test
    public void testRegisterFailure() throws ResponseException {
        RegisterRequest request = new RegisterRequest("", "", "");
        RegisterResult result = serverFacade.register(request);
        assertEquals("Bad Request", result.message());
    }

    @Test
    public void testLoginSuccess() throws ResponseException {
        RegisterRequest regRequest = new RegisterRequest("testUser", "password", "email@example.com");
        serverFacade.register(regRequest);

        LoginRequest request = new LoginRequest("testUser", "password");
        LoginResult result = serverFacade.login(request);
        assertTrue(result.success());
        assertEquals("testUser", result.username());
    }

    @Test
    public void testLoginFailure() throws ResponseException {
        LoginRequest request = new LoginRequest("nonExistentUser", "wrongPassword");
        LoginResult result = serverFacade.login(request);
        assertFalse(result.success());
    }

    @Test
    public void testLogoutSuccess() throws ResponseException {
        RegisterRequest regRequest = new RegisterRequest("testUser", "password", "email@example.com");
        serverFacade.register(regRequest);

        LoginRequest loginRequest = new LoginRequest("testUser", "password");
        LoginResult loginResult = serverFacade.login(loginRequest);

        LogoutRequest request = new LogoutRequest(loginResult.authToken());
        LogoutResult result = serverFacade.logout(request);
        assertTrue(result.success());
    }

    @Test
    public void testLogoutFailure() throws ResponseException {
        LogoutRequest request = new LogoutRequest("invalidAuthToken");
        LogoutResult result = serverFacade.logout(request);
        assertFalse(result.success());
    }

    @Test
    public void testCreateGameSuccess() throws ResponseException {
        RegisterRequest regRequest = new RegisterRequest("testUser", "password", "email@example.com");
        serverFacade.register(regRequest);

        LoginRequest loginRequest = new LoginRequest("testUser", "password");
        LoginResult loginResult = serverFacade.login(loginRequest);

        CreateGameRequest request = new CreateGameRequest(loginResult.authToken(), "ChessGame");
        CreateGameResult result = serverFacade.createGame(request);
        assertTrue(result.success());
    }

    @Test
    public void testCreateGameFailure() throws ResponseException {
        CreateGameRequest request = new CreateGameRequest("invalidAuthToken", "ChessGame");
        CreateGameResult result = serverFacade.createGame(request);
        assertFalse(result.success());
    }

    @Test
    public void testListGamesSuccess() throws ResponseException {
        RegisterRequest regRequest = new RegisterRequest("testUser", "password", "email@example.com");
        serverFacade.register(regRequest);
        LoginRequest loginRequest = new LoginRequest("testUser", "password");
        LoginResult loginResult = serverFacade.login(loginRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(loginResult.authToken(), "TestGame");
        serverFacade.createGame(createGameRequest);

        // List games
        ListGamesRequest request = new ListGamesRequest(loginResult.authToken());
        ListGamesResult result = serverFacade.listGames(request);
        assertTrue(result.success());
        assertNotNull(result.games());
        assertTrue(result.games().size() > 0);
    }

    @Test
    public void testListGamesFailure() throws ResponseException {
        ListGamesRequest request = new ListGamesRequest("invalidAuthToken"); // Invalid auth token
        ListGamesResult result = serverFacade.listGames(request);
        assertFalse(result.success());
        assertNull(result.games());
    }

    @Test
    public void testJoinGameSuccess() throws ResponseException {
        RegisterRequest regRequest = new RegisterRequest("testUser", "password", "email@example.com");
        serverFacade.register(regRequest);
        LoginRequest loginRequest = new LoginRequest("testUser", "password");
        LoginResult loginResult = serverFacade.login(loginRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(loginResult.authToken(), "JoinableGame");
        CreateGameResult createGameResult = serverFacade.createGame(createGameRequest);

        assertTrue(createGameResult.success());

        JoinGameRequest request = new JoinGameRequest(loginResult.authToken(), "white",
                String.valueOf(createGameResult.gameID()));
        JoinGameResult result = serverFacade.joinGame(request);
        assertTrue(result.success());
    }

    @Test
    public void testJoinGameFailure() throws ResponseException {
        JoinGameRequest request = new JoinGameRequest("invalidAuthToken", "White", "9999");
        JoinGameResult result = serverFacade.joinGame(request);
        assertFalse(result.success());
    }
}
