package com.persistent;
import java.util.Iterator;
import java.util.List;

public class Team {

    private String teamsName;
    private  List<User> users;


    /**
     * The function is adding a user to the list, due to request from TeamManager Class.
     * @param user The user that is being added to the list.
     */
    public void addUser(User user)
    {
        users.add(user);//The function operates only if the user is exist.(Checked in the TeamManager Class)
    }


    /**
     * The function is removing a user from the list
     * @param user The user we want to delete
     */
    public void removeUser(User user)
    {
        users.remove(user);
    }
}
