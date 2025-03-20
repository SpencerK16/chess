package ui;


import java.util.Arrays;

import exception.ResponseException;
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
                    case "register" -> register(params);
                    case "login" -> login(params);
                    case "help" -> helpLoggedOut();
                    case "quit" -> "quit";
                    default -> "Invalid command. Type 'help' for a list of commands.";
                };
            } else {
                return switch (cmd) {
                    case "create" -> create(params);
                    case "list" -> list();
                    case "join" -> join(params);
                    case "observe" -> observe(params);
                    case "logout" -> logout();
                    case "help" -> helpLoggedIn();
                    case "quit" -> "quit";
                    default -> "Invalid command. Type 'help' for a list of commands.";
                };
            }
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
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