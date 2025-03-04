package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class DataStore {
    static final Map<String, UserData> USERS;
    static final Map<Integer, GameData> GAMES;
    static final Map<String, String> AUTHTOKENS;

    static {
        USERS = new HashMap<>();
        GAMES = new HashMap<>();
        AUTHTOKENS = new HashMap<>();
    }
}
