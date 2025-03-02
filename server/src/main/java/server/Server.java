package server;

import handlers.*;
import results.ClearResult;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", ClearHandler::processRequest);
        Spark.post("/user", RegisterHandler::processRequest);
        Spark.post("/session", LoginHandler::processRequest);
        Spark.delete("/session", LogoutHandler::processRequest);
        Spark.get("/game", ListGamesHandler::processRequest);
        Spark.post("/game", CreateGameHandler::processRequest);
        Spark.put("/game", JoinGameHandler::processRequest);




        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}
