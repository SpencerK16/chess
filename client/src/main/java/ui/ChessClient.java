package ui;


import java.util.*;

import chess.*;
import exception.ResponseException;
import model.GameData;
import request.*;
import websocket.WSClient;


public class ChessClient {

    private State state = State.LOGGEDOUT;
    public ServerFacade server;
    private String authtoken = "";
    private String username = "";
    private int gameID;
    private WSClient ws;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
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
            } else if (state == State.LOGGEDIN) {
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
            } else {
                return switch (cmd) {
                    case "redraw" -> redrawCommand();
                    case "leave" -> leaveCommand();
                    case "move" -> moveCommand(params);
                    case "resign" -> resignCommand();
                    case "highlight" -> highlightCommand(params);
                    case "help" -> helpInGame();
                    default -> "Invalid command. Type 'help' for a list of commands.";
                };
            }
        } catch (ResponseException ex) {
            return ex.toString();
        }
    }

    private String highlightCommand(String[] params) {
        if (params == null || params.length != 1) {
            return "Usage: highlight <POSITION>";
        }

        String posParam = params[0].toLowerCase();

        if (!isValidPosition(posParam)) {
            return "Error: Invalid position. Must be a-h followed by a number 1-8.";
        }
        char colChar = posParam.charAt(0);
        int colIndex = colChar - 'a' + 1;
        int row = posParam.charAt(1) - '0';
        ChessPosition position = new ChessPosition(row, colIndex);

        GameData game = null;
        var request = new ListGamesRequest(authtoken);
        var result = server.listGames(request);

        for(GameData g : result.games()) {
            if(g.gameID() == gameID) {
                game = g;
                break;
            }
        }

        if(game == null) return " ";

        ChessPiece piece = game.game().getBoard().getPiece(position);
        if (piece == null) {
            return "Error: There is no piece at " + posParam + ".";
        }

        Collection<ChessPosition> toHighlight = new LinkedList<ChessPosition>();
        for(ChessMove cM : piece.pieceMoves(game.game().getBoard(), position))
            toHighlight.add(cM.getEndPosition());

//        toHighlight.add(position);

        try {
            BoardMaker.makeBoard(game.game().getBoard(),
                    (Objects.equals(game.whiteUsername(), username)), toHighlight);
            return " ";
        } catch(Exception e) {
            return "Error: Unable.";
        }
    }

    private String resignCommand() {
        // How can I end a game?
        return "TODO";
    }

    private String moveCommand(String[] params) {
//            if (params == null || params.length != 2) {
//                return "Usage: move <START> <END>";
//            }
//
//            String startParam = params[0].toLowerCase();
//            if (!isValidPosition(startParam)) {
//                return "Error: Invalid starting position. Must be a-h followed by a number 1-8.";
//            }
//
//            String endParam = params[1].toLowerCase();
//            if (!isValidPosition(endParam)) {
//                return "Error: Invalid ending position. Must be a-h followed by a number 1-8.";
//            }
//
//            char startColChar = startParam.charAt(0);
//            int startColIndex = startColChar - 'a' + 1;
//            int startRow = startParam.charAt(1) - '0';
//            ChessPosition startPosition = new ChessPosition(startRow, startColIndex);
//
//            char endColChar = endParam.charAt(0);
//            int endColIndex = endColChar - 'a' + 1;
//            int endRow = endParam.charAt(1) - '0';
//            ChessPosition endPosition = new ChessPosition(endRow, endColIndex);

            return "TODO: Implement move from "; //+ startParam + " to " + endParam + ".";
    }

    private String leaveCommand() {
        //Send in a blank?
        return "TODO";
    }

    private String redrawCommand() {

        GameData game = null;
        var request = new ListGamesRequest(authtoken);
        var result = server.listGames(request);

        for(GameData g : result.games()) {
            if(g.gameID() == gameID) {
                game = g;
                break;
            }
        }

        if(game == null) return " ";

        try {
            BoardMaker.makeBoard(game.game().getBoard(),
                    (Objects.equals(game.whiteUsername(), username)), null);
            return " ";
        } catch(Exception e) {
            return "Error: Unable.";
        }
    }

    private String registerCommand(String[] params) throws ResponseException {
        if (params.length < 3) {
            return "Usage: register <USERNAME> <PASSWORD> <EMAIL>";
        }
        var request = new RegisterRequest(params[0], params[1], params[2]);
        var result = server.register(request);
        if (result.success()) {
            state = State.LOGGEDIN;
            authtoken = result.authToken();
            username = result.username();
            return "Registration successful. You are now logged in!";
        }
        return "Registration failed: " + result.message();
    }

    private String loginCommand(String[] params) throws ResponseException {
        if (params.length < 2) {
            return "Usage: login <USERNAME> <PASSWORD>";
        }

        var request = new LoginRequest(params[0], params[1]);
        var result = server.login(request);
        if (result.success()) {
            state = State.LOGGEDIN;
            authtoken = result.authToken();
            username = result.username();
            return "Login successful. Welcome!";
        }
        return "Login failed: " + result.message();
    }

    private String logoutCommand() throws ResponseException {
        assertSignedIn();
        var request = new LogoutRequest(authtoken);
        var result = server.logout(request);
        if (result.success()) {
            state = State.LOGGEDOUT;
            authtoken = "";
            username = "";
            return "Logged out successfully. See you next time!";
        }
        return "Logout failed: " + result.message();
    }

    private String createCommand(String[] params) throws ResponseException {
        assertSignedIn();
        if (params.length < 1) {
            return "Usage: create <NAME>";
        }
        var request = new CreateGameRequest(authtoken, params[0]);
        var result = server.createGame(request);
        if (result.success()) {
            return "Game created successfully: " + params[0];
        }
        return "Failed to create game: " + result.message();
    }

    private String listCommand() throws ResponseException {
        assertSignedIn();
        var request = new ListGamesRequest(authtoken);
        var result = server.listGames(request);
        if (result.success()) {
            var games = result.games();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < games.size(); i++) {
                sb.append(i + 1).append(". ").append(games.get(i).gameName())
                        .append(" (Players: ").append(games.get(i).whiteUsername())
                        .append(", ").append(games.get(i).blackUsername()).append(")\n");
            }
            return sb.toString();
        }
        return "Failed to list games: " + result.message();
    }

    private String joinCommand(String[] params) throws ResponseException {
        assertSignedIn();
        if (params.length < 2) {  // Ensure we have at least two parameters
            return "Usage: join <GAME_NUMBER> <WHITE | BLACK>";
        }
        state = State.INGAME;
        int gameIndex = Integer.parseInt(params[0]) - 1;

        var request = new ListGamesRequest(authtoken);
        var result = server.listGames(request);

        int gameId = result.games().get(gameIndex).gameID();
        gameID = gameId;
        JoinGameRequest jgRequest;
        boolean isWhite;
        if (params[1].equalsIgnoreCase("white")) {
            isWhite = true;
            jgRequest = new JoinGameRequest(authtoken, ChessGame.TeamColor.WHITE.toString(), Integer.toString(gameId));
        } else if (params[1].equalsIgnoreCase("black")) {
            isWhite = false;
            jgRequest = new JoinGameRequest(authtoken, ChessGame.TeamColor.BLACK.toString(), Integer.toString(gameId));
        } else {
            return "Usage: join <GAME_NUMBER> <WHITE | BLACK>";
        }

        var joinResult = server.joinGame(jgRequest);
        if (joinResult.success()) {
            BoardMaker.makeBoard(result.games().get(gameIndex).game().getBoard(), isWhite, null);
            return "Joined game successfully!";
        }
        return "Failed to join game: " + joinResult.message();
    }


    private String observe(String[] params) {
        if (params == null || params.length == 0) {
            return "Error: No GameID provided. Please choose a GameID.";
        }

        int gameIndex;
        try {
            gameIndex = Integer.parseInt(params[0]) - 1;
        } catch (NumberFormatException e) {
            return "Error: Invalid GameID format. Please enter a numeric GameID.";
        }

        try {
            var request = new ListGamesRequest(authtoken);
            var result = server.listGames(request);

            BoardMaker.makeBoard(result.games().get(gameIndex).game().getBoard(),
                    (Objects.equals(result.games().get(gameIndex).whiteUsername(), username)), null);
            return "Observe will be made in phase 6.";
        } catch(Exception e) {
            return "Error: Unable to observe game. Please ensure you have chosen a valid GameID.";
        }
    }


    public boolean loggedIn() {
        return state == State.LOGGEDIN;
    }

    public boolean inGame() {
        return state == State.INGAME;
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
        join <ID> <WHITE | BLACK> - a game
        observe <ID> - a game
        logout - when you are done
        quit - playing chess
        help - with possible commands
        """;
    }

    String helpInGame() {
        return """
        help - with possible commands
        redraw - redraws the board
        leave - removes the user from the game
        move - moves a piece
        resign - user forfeits the game
        highlight - shows legal move for a piece
        """;
    }


    private void assertSignedIn() throws ResponseException {
        if (state == State.LOGGEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }

    private boolean isValidPosition(String pos) {
        if (pos == null || pos.length() != 2) {
            return false;
        }
        char col = pos.charAt(0);
        char row = pos.charAt(1);
        return (col >= 'a' && col <= 'h') && (row >= '1' && row <= '8');
    }
}