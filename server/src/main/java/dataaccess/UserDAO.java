package dataaccess;

import model.UserData;
import java.util.Map;
import java.util.HashMap;

public class UserDAO {

    public UserDAO() {
    }

    //Create User
    public void insertUser(UserData user) throws  DataAccessException{
        if (DataStore.users.containsKey(user.username())) {
            throw new DataAccessException("User already exists.");
        }

        DataStore.users.put(user.username(), user);
    }

    //Read information about users
    public UserData getUser(String username) throws DataAccessException{
        if (DataStore.users.containsKey(username)) {
            return DataStore.users.get(username);
        }
        throw new DataAccessException("User doesn't exist.");
    }

    public boolean usernameExists(String username) throws DataAccessException {
        return DataStore.users.containsKey(username);
    }

    //Update information already in user
    public void updateUser(UserData user) throws DataAccessException{
        //Get new string and convert it to needed change
        if (DataStore.users.containsKey(user.username())) {
            DataStore.users.put(user.username(), user);
        }
        throw new DataAccessException("User doesn't exist.");
    }

    //Deletes a user
    public void deleteUser(String username) throws DataAccessException{
        //Pick which uses should be deleted and then deletes it
        if (DataStore.users.containsKey(username)) {
            DataStore.users.remove(username);
        }
        throw new DataAccessException("User doesn't exist.");
    }

    public void clear() throws DataAccessException {
        DataStore.users.clear();
    }

}

//String username, String password, String email

/*
Create objects in the data store
Read objects from the data store
Update objects already in the data store
Delete objects from the data store
*/