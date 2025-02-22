package dataaccess;

import model.GameData;

import java.util.Map;
import java.util.HashMap;

public class GameDAO {
    private Map<Integer, GameData> games;

    public GameDAO(){
        games = new HashMap<>();
    }

    public void insertGame(GameData gameData) throws DataAccessException {
        if (games.containsKey(gameData.gameID())) {
            throw new DataAccessException("Game with this ID already exists.");
        }
        games.put(gameData.gameID(), gameData);
    }

    // Read GameData based on username
    public GameData getGame(int gameID) throws DataAccessException {
        if (games.containsKey(gameID)) {
            return games.get(gameID);
        }
        throw new DataAccessException("Game with this ID doesn't exist.");
    }

    // Update existing game
    public void updateGame(GameData gameData) throws DataAccessException {
        if (games.containsKey(gameData.gameID())) {
            games.put(gameData.gameID(), gameData);
        }
        throw new DataAccessException("User doesn't exist.");
    }

    // Delete Game based on gameID
    public void deleteGame(int gameID) throws DataAccessException {
        if (games.containsKey(gameID)) {
            games.remove(gameID);
        }
        throw new DataAccessException("Game with this ID doesn't exist.");

    }
}