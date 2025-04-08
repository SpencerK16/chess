package websocket;

import chess.ChessBoard;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                System.out.print("make a move\n");
//                1. Server verifies the validity of the move.
//                2. Game is updated to represent the move. Game is updated in the database.
//                3. Server sends a LOAD_GAME message to all clients in the game (including the root client) with an updated game.
//                4. Server sends a Notification message to all other clients in that game informing them what move was made.
//                5. If the move results in check, checkmate or stalemate the server sends a Notification message to all clients.
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