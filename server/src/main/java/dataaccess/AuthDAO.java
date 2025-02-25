package dataaccess;

import model.AuthData;

import java.util.Map;
import java.util.HashMap;

public class AuthDAO {

    private Map<String, AuthData> authTokens;

    public AuthDAO() {
        authTokens = new HashMap<>();
    }

    public void insertAuth(AuthData authData) throws DataAccessException {
        if (authTokens.containsKey(authData.username())) {
            throw new DataAccessException("User already exists.");
        }
        authTokens.put(authData.username(), authData);
    }

    // Read AuthData based on username
    public AuthData getAuth(String username) throws DataAccessException {
        if (authTokens.containsKey(username)) {
            return authTokens.get(username);
        }
        throw new DataAccessException("AuthToken doesn't exist.");
    }

    // Update AuthData
    public void updateAuth(AuthData authData) throws DataAccessException {
        if (authTokens.containsKey(authData.username())) {
            authTokens.put(authData.username(), authData);
        }
        throw new DataAccessException("User doesn't exist.");
    }

    // Delete AuthData based on Username
    public void deleteAuth(String username) throws DataAccessException {
        if (authTokens.containsKey(username)) {
            authTokens.remove(username);
        }
        throw new DataAccessException("User doesn't exist.");

    }
}
//String authToken, String username

/*Create objects in the data store
Read objects from the data store
Update objects already in the data store
Delete objects from the data store*/