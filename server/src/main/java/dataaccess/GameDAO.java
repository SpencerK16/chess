package dataaccess;

import model.GameData;

import java.util.*;

public class GameDAO {
    public GameDAO(){
    }

    public List<String> getUserGames(String user) {
        List<String> toReturn = new LinkedList<>();
        for(Map.Entry<Integer, GameData> e : DataStore.GAMES.entrySet()) {
            if(Objects.equals(e.getValue().whiteUsername(), user) || Objects.equals(e.getValue().blackUsername(), user)) {
                toReturn.add(Integer.toString(e.getValue().gameID()));
            }
        }

        return toReturn;
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

//    // Update existing game
//    public void updateGame(GameData gameData) throws DataAccessException {
//        if (DataStore.GAMES.containsKey(gameData.gameID())) {
//            DataStore.GAMES.put(gameData.gameID(), gameData);
//        }
//        throw new DataAccessException("User doesn't exist.");
//    }
//
//    // Delete Game based on gameID
//    public void deleteGame(int gameID) throws DataAccessException {
//        if (DataStore.GAMES.containsKey(gameID)) {
//            DataStore.GAMES.remove(gameID);
//        }
//        throw new DataAccessException("Game with this ID doesn't exist.");
//
//    }

    public void clear() throws DataAccessException {
        DataStore.GAMES.clear();
    }

    public List<GameData> getGamesById(List<String> ids) {
        List<GameData> toReturn = new LinkedList<>();

        for(GameData d : DataStore.GAMES.values()) {
            if(ids.contains(Integer.toString(d.gameID()))) {
                toReturn.add(d);
            }
        }

        return toReturn;
    }
}