package websocket;

import chess.ChessBoard;
import chess.*;
import chess.ChessBoardAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.*;

@WebSocket
public class WSServer {
    public static void main(String[] args) {
//        Spark.port(8080);
//        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
    }

    private Map<String, Session> sessionCollection = new HashMap<>();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s \n", message);
        UserGameCommand ugc = new Gson().fromJson(message, UserGameCommand.class);
        switch (ugc.getCommandType()) {
            case CONNECT -> processConnect(ugc, session);

            case MAKE_MOVE -> {
                //System.out.print("make a move\n");

                GameDAO gameDAO = new GameDAO();
                GameData gameData;
                try {
                    gameData = gameDAO.getGame(ugc.getGameID());
                } catch (DataAccessException ex) {
                    ServerMessage errorMsg = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                    errorMsg.errorMessage = "Error: Game not found.";
                    session.getRemote().sendString(new Gson().toJson(errorMsg));
                    break;
                }
                AuthDAO authDAO = new AuthDAO();
                AuthData userData = null;
                try {
                    userData = authDAO.getAuth(ugc.getAuthToken());
                } catch (DataAccessException ex) {
                    ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                    errorMessage.errorMessage = "error user unauthorized!";
                    String messageString = new Gson().toJson(errorMessage);
                    session.getRemote().sendString(messageString);
                    return;
                }

                ChessGame chessGame = gameData.game();
                ChessMove requestedMove = ugc.move;

                if (chessGame.isInCheckmate(ChessGame.TeamColor.WHITE) ||
                        chessGame.isInCheckmate(ChessGame.TeamColor.BLACK) ||
                        chessGame.isInStalemate(ChessGame.TeamColor.WHITE) ||
                        chessGame.isInStalemate(ChessGame.TeamColor.BLACK)) {

                    ServerMessage errorMsg = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                    errorMsg.errorMessage = "Error users can't when game is over";
                    session.getRemote().sendString(new Gson().toJson(errorMsg));
                    return;
                }
                boolean isWhitePlayer = userData.username().equals(gameData.whiteUsername());
                boolean isBlackPlayer = userData.username().equals(gameData.blackUsername());

                try {
                    if (isWhitePlayer || isBlackPlayer){
                        if ((isWhitePlayer && chessGame.getTeamTurn() == ChessGame.TeamColor.WHITE) || (isBlackPlayer
                                && chessGame.getTeamTurn() == ChessGame.TeamColor.BLACK)) {
                            chessGame.makeMove(requestedMove);
                            gameDAO.updateGame(gameData);
                        } else {
                            ServerMessage errorMsg = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                            errorMsg.errorMessage = "Error users can't move for opponent";
                            session.getRemote().sendString(new Gson().toJson(errorMsg));
                            return;
                        }

                    } else {
                        ServerMessage errorMsg = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                        errorMsg.errorMessage = "Error observers cannot make move";
                        session.getRemote().sendString(new Gson().toJson(errorMsg));
                        return;
                    }

                } catch (InvalidMoveException ex) {
                    ServerMessage errorMsg = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                    errorMsg.errorMessage = "Invalid move: " + ex.getMessage();
                    session.getRemote().sendString(new Gson().toJson(errorMsg));
                    break;
                } catch (Exception ex) {
                    ServerMessage errorMsg = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                    errorMsg.errorMessage = "Error while moving: " + ex.getMessage();
                    session.getRemote().sendString(new Gson().toJson(errorMsg));
                    break;
                }

                ServerMessage loadGameMsg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
                loadGameMsg.game = gameData;

                List<String> removeList = new ArrayList<>();
                for (String otherAuthToken : sessionCollection.keySet()) {
                    Session otherSession = sessionCollection.get(otherAuthToken);
                    if (otherSession.isOpen()) {
                        otherSession.getRemote().sendString(new Gson().toJson(loadGameMsg));
                        System.out.print("Sent load game to " + otherAuthToken + "\n");
                    } else {
                        removeList.add(otherAuthToken);
                    }
                }
                for (String token : removeList) {
                    sessionCollection.remove(token);
                }

                ServerMessage notifyMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                notifyMsg.message = "Move: "
                        + requestedMove.getStartPosition()
                        + " to " + requestedMove.getEndPosition();

                removeList.clear();
                for (String otherAuthToken : sessionCollection.keySet()) {
                    Session otherSession = sessionCollection.get(otherAuthToken);
                    if (otherSession.isOpen()) {
                        if (!Objects.equals(otherAuthToken, ugc.getAuthToken())) {
                            otherSession.getRemote().sendString(new Gson().toJson(notifyMsg));
                            System.out.print("Sent notification to " + otherAuthToken  + "\n");
                        }

                    } else {
                        removeList.add(otherAuthToken);
                    }
                }
                for (String token : removeList) {
                    sessionCollection.remove(token);
                }

                if (chessGame.isInCheckmate(chessGame.getTeamTurn())) {
                    ServerMessage mateMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                    mateMsg.message = "Checkmate! Game over.";
                    for (String otherAuthToken : sessionCollection.keySet()) {
                        Session otherSession = sessionCollection.get(otherAuthToken);
                        if (otherSession.isOpen()) {
                            otherSession.getRemote().sendString(new Gson().toJson(mateMsg));
                        }
                    }
                } else if (chessGame.isInCheck(chessGame.getTeamTurn())) {
                    ServerMessage checkMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                    checkMsg.message = "Check!";
                    for (String otherAuthToken : sessionCollection.keySet()) {
                        Session otherSession = sessionCollection.get(otherAuthToken);
                        if (otherSession.isOpen()) {
                            otherSession.getRemote().sendString(new Gson().toJson(checkMsg));
                        }
                    }
                }

                if (chessGame.isInStalemate(chessGame.getTeamTurn())) {
                    ServerMessage stalemateMsg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                    stalemateMsg.message = "Stalemate! Game drawn.";
                    for (String otherAuthToken : sessionCollection.keySet()) {
                        Session otherSession = sessionCollection.get(otherAuthToken);
                        if (otherSession.isOpen()) {
                            otherSession.getRemote().sendString(new Gson().toJson(stalemateMsg));
                        }
                    }
                }
            }
            case LEAVE -> {
            }
            case RESIGN -> {
            }

        };
//        session.getRemote().sendString("WebSocket response: " + message);
    }

    private void processConnect (UserGameCommand ugc, Session session) throws IOException {
        //see if user valid
        AuthDAO authDAO = new AuthDAO();
        AuthData userData = null;
        try {
            userData = authDAO.getAuth(ugc.getAuthToken());
        } catch (DataAccessException ex) {
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.errorMessage = "error user unauthorized!";
            String messageString = new Gson().toJson(errorMessage);
            session.getRemote().sendString(messageString);
            return;
        }
        //see if game exists
        GameDAO gameDAO = new GameDAO();
        GameData tempData = null;
        try{
            tempData = gameDAO.getGame(ugc.getGameID());
        } catch (DataAccessException ex) {
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.errorMessage = "error game does not exist!";
            String messageString = new Gson().toJson(errorMessage);
            session.getRemote().sendString(messageString);
            return;
        }
            System.out.print("Received \n");

        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        msg.game = tempData;
        try{
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(ChessBoard.class, new ChessBoardAdapter());
            Gson gson = gsonBuilder.create();
            String jsonMessage = gson.toJson(msg);

            session.getRemote().sendString(jsonMessage);
            List<String> removeList = new ArrayList<>();
            for (String otherAuthKey : sessionCollection.keySet()) {
                Session otherSession = sessionCollection.get(otherAuthKey);
                if (otherSession.isOpen()) {
                    ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                    //is user a player or an observer
                    if (tempData.blackUsername() == userData.username()) {
                        notificationMessage.message = userData.username() + " joined games as black";
                    } else if (tempData.whiteUsername() == userData.username()) {
                        notificationMessage.message = userData.username() + " joined games as white";
                    } else {
                        notificationMessage.message = userData.username() + " joined game as an observer";
                    }
                    String notification = new Gson().toJson(notificationMessage);
                    otherSession.getRemote().sendString(notification);
                } else {
                    removeList.add(otherAuthKey);
                }
            }
            for (String removeAuthKey : removeList) {
                sessionCollection.remove(removeAuthKey);
            }
            sessionCollection.put(ugc.getAuthToken(), session);

        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }

        //set the game

    }
}