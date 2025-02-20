package dataaccess;

import model.UserData;

public class UserDAO {

    //Create User
    public void insertUser(UserData user) throws  DataAccessException{
        //Convert the inputs strings and set them to be user.username(), user.password(), and user.email().
    }

    //Read information about users
    public UserData getUser(String username) throws DataAccessException{
        //return UserData in form of a string
        return new UserData("placeholder_username", "placeholder_password", "placeholder@email.com");
    }

    //Update information already in user
    public void updateUser(UserData user) throws DataAccessException{
        //Get new string and convert it to needed change
    }
    //Deletes a user
    public void deleteUser(String username) throws DataAccessException{
        //Pick which uses should be deleted and then deletes it
    }

}

//String username, String password, String email

/*
Create objects in the data store
Read objects from the data store
Update objects already in the data store
Delete objects from the data store
*/