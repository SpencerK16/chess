package dataaccess;

import model.UserData;
import java.util.Map;
import java.util.HashMap;

public class UserDAO {

    private Map<String, UserData> users;

    public UserDAO() {
        users = new HashMap<>();
    }

    //Create User
    public void insertUser(UserData user) throws  DataAccessException{
        if (users.containsKey(user.username())) {
            throw new DataAccessException("User already exists.");
        }
        users.put(user.username(), user);
    }

    //Read information about users
    public UserData getUser(String username) throws DataAccessException{
        if (users.containsKey(username)) {
            return users.get(username);
        }
        throw new DataAccessException("User doesn't exist.");
    }

    //Update information already in user
    public void updateUser(UserData user) throws DataAccessException{
        //Get new string and convert it to needed change
        if (users.containsKey(user.username())) {
            users.put(user.username(), user);
        }
        throw new DataAccessException("User doesn't exist.");
    }

    //Deletes a user
    public void deleteUser(String username) throws DataAccessException{
        //Pick which uses should be deleted and then deletes it
        if (users.containsKey(username)) {
            users.remove(username);
        }
        throw new DataAccessException("User doesn't exist.");
    }

    public void clear() throws DataAccessException {
        users.clear();
    }

}

//String username, String password, String email

/*
Create objects in the data store
Read objects from the data store
Update objects already in the data store
Delete objects from the data store
*/