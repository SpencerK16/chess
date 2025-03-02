package dataaccess;

import model.UserData;

public class UserDAO {

    public UserDAO() {
    }

    //Create User
    public void insertUser(UserData user) throws  DataAccessException{
        if (DataStore.USERS.containsKey(user.username())) {
            throw new DataAccessException("User already exists.");
        }

        DataStore.USERS.put(user.username(), user);
    }

    //Read information about users
    public UserData getUser(String username) throws DataAccessException{
        if (DataStore.USERS.containsKey(username)) {
            return DataStore.USERS.get(username);
        }
        throw new DataAccessException("User doesn't exist.");
    }

    public boolean usernameExists(String username) throws DataAccessException {
        return DataStore.USERS.containsKey(username);
    }

    //Update information already in user
//    public void updateUser(UserData user) throws DataAccessException{
//        //Get new string and convert it to needed change
//        if (DataStore.USERS.containsKey(user.username())) {
//            DataStore.USERS.put(user.username(), user);
//        }
//        throw new DataAccessException("User doesn't exist.");
//    }

//    //Deletes a user
//    public void deleteUser(String username) throws DataAccessException{
//        //Pick which uses should be deleted and then deletes it
//        if (DataStore.USERS.containsKey(username)) {
//            DataStore.USERS.remove(username);
//        }
//        throw new DataAccessException("User doesn't exist.");
//    }

    public void clear() throws DataAccessException {
        DataStore.USERS.clear();
    }

}

//String username, String password, String email

/*
Create objects in the data store
Read objects from the data store
Update objects already in the data store
Delete objects from the data store
*/