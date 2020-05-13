package com.persistent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Team {

    private String teamsName;
    private  List<User> users;


    /**
     * The constructor of the class
     * @param teamsName the new team's name.
     */
    public Team(String teamsName)
    {
        this.teamsName=teamsName;
        this.users= new LinkedList<>();//Explicit type argument

    }

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



    public String getTeamsName(){
        return teamsName;
    }

    public void setTeamsName(String teamsName){
        this.teamsName = teamsName;
    }

    public List<User> getUsers(){
        return users;
    }
}
