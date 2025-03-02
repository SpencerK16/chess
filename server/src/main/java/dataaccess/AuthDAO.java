package dataaccess;

import model.AuthData;

import java.util.Map;
import java.util.Objects;

public class AuthDAO {


    public AuthDAO() {
    }

    public void insertAuth(AuthData authData) throws DataAccessException {
        if (DataStore.AUTHTOKENS.containsKey(authData.username())) {
            DataStore.AUTHTOKENS.remove(authData.username());
        }
        DataStore.AUTHTOKENS.put(authData.username(), authData);
    }

    // Read AuthData based on username
    public AuthData getAuth(String authtoken) throws DataAccessException {
        for(AuthData a : DataStore.AUTHTOKENS.values()) {
            if(Objects.equals(a.authToken(), authtoken)){
                return a;
            }
        }

        throw new DataAccessException("AuthToken doesn't exist.");
    }

    public String getUser(String authData) {
        for(Map.Entry<String, AuthData> e : DataStore.AUTHTOKENS.entrySet()) {
            if(Objects.equals(e.getValue().authToken(), authData)) {
                return e.getKey();
            }
        }

        return "";
    }

    // Update AuthData
//    public void updateAuth(AuthData authData) throws DataAccessException {
//        if (DataStore.AUTHTOKENS.containsKey(authData.username())) {
//            DataStore.AUTHTOKENS.put(authData.username(), authData);
//        }
//        throw new DataAccessException("User doesn't exist.");
//    }

    // Delete AuthData based on Username
    public void deleteAuth(String username) throws DataAccessException {
        if (DataStore.AUTHTOKENS.containsKey(username)) {
            DataStore.AUTHTOKENS.remove(username);
            return;
        }
        throw new DataAccessException("User doesn't exist.");

    }

    public void clear() throws DataAccessException {
        DataStore.AUTHTOKENS.clear();
    }
}
//String authToken, String username

/*Create objects in the data store
Read objects from the data store
Update objects already in the data store
Delete objects from the data store*/