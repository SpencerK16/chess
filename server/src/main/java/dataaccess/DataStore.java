package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class DataStore {
    static final Map<String, UserData> users;
    static final Map<Integer, GameData> games;
    static final Map<String, AuthData> authTokens;

    static {
        users = new HashMap<>();
        games = new HashMap<>();
        authTokens = new HashMap<>();
    }
}
