package dataaccess;

import model.GameData;
import model.GameListData;

import java.util.*;

public class GameDAO {
    public GameDAO(){
    }


    public void insertGame(GameData gameData) throws DataAccessException {
        if (DataStore.GAMES.containsKey(gameData.gameID())) {
            throw new DataAccessException("Game with this ID already exists.");
        }
        DataStore.GAMES.put(gameData.gameID(), gameData);
    }

    // Read GameData based on username
    public GameData getGame(int gameID) throws DataAccessException {
        if (DataStore.GAMES.containsKey(gameID)) {
            return DataStore.GAMES.get(gameID);
        }
        throw new DataAccessException("Game with this ID doesn't exist.");
    }

    public void deleteGame(int gameID) {
        if (DataStore.GAMES.containsKey(gameID)) {
            DataStore.GAMES.remove(gameID);
        }

    }

    public void clear() throws DataAccessException {
        DataStore.GAMES.clear();
    }

    public List<GameListData> getGames() {
        List<GameListData> toReturn = new LinkedList<>();

        for(GameData d : DataStore.GAMES.values()) {
            String wUser = d.whiteUsername();
            String bUser = d.blackUsername();

            toReturn.add(new GameListData(d.gameID(), d.whiteUsername(), d.blackUsername(), d.gameName()));
        }

        return toReturn;
    }
}