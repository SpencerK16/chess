package dataaccess;

import model.AuthData;

import java.util.Map;

public class AuthDAO {


    public AuthDAO() {
    }

    public void insertAuth(AuthData authData) throws DataAccessException {
        DataStore.AUTHTOKENS.put(authData.authToken(), authData.username());
    }

    // Read AuthData based on username
    public AuthData getAuth(String authtoken) throws DataAccessException {
        if(DataStore.AUTHTOKENS.containsKey(authtoken)) {
            return new AuthData(DataStore.AUTHTOKENS.get(authtoken), authtoken);
        }

        throw new DataAccessException("AuthToken doesn't exist.");
    }

    public String getUser(String authData) {
        for(Map.Entry<String, String> e : DataStore.AUTHTOKENS.entrySet()) {
            e.getKey();
        }
        if(DataStore.AUTHTOKENS.containsKey(authData)) {
            return DataStore.AUTHTOKENS.get(authData);
        }

        return null;
    }

    public void deleteAuth(String authtoken) throws DataAccessException {
        if (DataStore.AUTHTOKENS.containsKey(authtoken)) {
            DataStore.AUTHTOKENS.remove(authtoken);
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