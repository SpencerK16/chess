package ui;


import java.util.Arrays;

import exception.ResponseException;
import request.*;
import results.*;
import server.ServerFacade;


public class ChessClient {

    private State state = State.LOGGEDOUT;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }


    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);

            if (state == State.LOGGEDOUT) {
                return switch (cmd) {
                    case "register" -> registerCommand(params);
                    case "login" -> loginCommand(params);
                    case "help" -> helpLoggedOut();
                    case "quit" -> "quit";
                    default -> "Invalid command. Type 'help' for a list of commands.";
                };
            } else {
                return switch (cmd) {
                    case "create" -> createCommand(params);
                    case "list" -> listCommand();
                    case "join" -> joinCommand(params);
                    case "observe" -> observe(params);
                    case "logout" -> logoutCommand();
                    case "help" -> helpLoggedIn();
                    case "quit" -> "quit";
                    default -> "Invalid command. Type 'help' for a list of commands.";
                };
            }
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String registerCommand(String[] params) throws ResponseException {
        if (params.length < 3) {
            return "Usage: register <USERNAME> <PASSWORD> <EMAIL>";
        }
        var request = new RegisterRequest(params[0], params[1], params[2]);
        var result = server.register(request);
        if (result.isSuccess()) {
            state = State.LOGGEDIN;
            return "Registration successful. You are now logged in!";
        }
        return "Registration failed: " + result.getMessage();
    }

    private String loginCommand(String[] params) throws ResponseException {
        if (params.length < 2) {
            return "Usage: login <USERNAME> <PASSWORD>";
        }
        var request = new LoginRequest(params[0], params[1]);
        var result = server.login(request);
        if (result.isSuccess()) {
            state = State.LOGGEDIN;
            return "Login successful. Welcome!";
        }
        return "Login failed: " + result.getMessage();
    }

    private String logoutCommand() throws ResponseException {
        assertSignedIn();
        var request = new LogoutRequest();
        var result = server.logout(request);
        if (result.isSuccess()) {
            state = State.LOGGEDOUT;
            return "Logged out successfully. See you next time!";
        }
        return "Logout failed: " + result.getMessage();
    }

    private String createCommand(String[] params) throws ResponseException {
        assertSignedIn();
        if (params.length < 1) {
            return "Usage: create <NAME>";
        }
        var request = new CreateGameRequest(params[0]);
        var result = server.createGame(request);
        if (result.isSuccess()) {
            return "Game created successfully: " + result.getGameName();
        }
        return "Failed to create game: " + result.getMessage();
    }

    private String listCommand() throws ResponseException {
        assertSignedIn();
        var request = new ListGamesRequest();
        var result = server.listGames(request);
        if (result.isSuccess()) {
            var games = result.getGames();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < games.size(); i++) {
                sb.append(i + 1).append(". ").append(games.get(i).getName())
                        .append(" (Players: ").append(games.get(i).getPlayers()).append(")\n");
            }
            return sb.toString();
        }
        return "Failed to list games: " + result.getMessage();
    }

    private String joinCommand(String[] params) throws ResponseException {
        assertSignedIn();
        if (params.length < 1) {
            return "Usage: join <GAME_NUMBER>";
        }
        int gameIndex = Integer.parseInt(params[0]) - 1;
        var gameId = savedGameList.get(gameIndex).getId();
        var request = new JoinGameRequest(gameId);
        var result = server.joinGame(request);
        if (result.isSuccess()) {
            return "Joined game successfully!";
        }
        return "Failed to join game: " + result.getMessage();
    }

    private String observe(String[] params) {
        return "Observe will be made in phase 6.";
    }




    String helpLoggedOut() {
        return """
        register <USERNAME> <PASSWORD> <EMAIL> - to create an account
        login <USERNAME> <PASSWORD> - to play chess
        quit - playing chess
        help - with possible commands
        """;
    }

    String helpLoggedIn() {
        return """
        create <NAME> - a game
        list - games
        join <ID> - a game
        observe <ID> - a game
        logout - when you are done
        quit - playing chess
        help - with possible commands
        """;
    }


    private void assertSignedIn() throws ResponseException {
        if (state == State.LOGGEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}