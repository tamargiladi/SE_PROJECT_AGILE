package com.persistent;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.io.Serializable;

public class Team implements Serializable {

    private String teamsName;
    private List<String> users;


    /**
     * The constructor of the class
     *
     * @param teamsName the new team's name.
     *///
    public Team(String teamsName) {
        this.teamsName = teamsName;
        this.users = new LinkedList<>();//Explicit type argument

    }

    public Team(Team tm) {
        this.teamsName = tm.teamsName;
        this.users.addAll(tm.users);
    }

    /**
     * The function is adding a user to the list, due to request from TeamManager Class.
     *
     * @param username The user that is being added to the list.
     */
    public void addUser(String username) {
        users.add(username);//The function operates only if the user is exist.(Checked in the TeamManager Class)
    }


    /**
     * The function is removing a user from the list
     *
     * @param username The user we want to delete
     */
    public void removeUser(String username) {
        users.remove(username);
    }


    public String getTeamsName() {
        return teamsName;
    }

    public void setTeamsName(String newTeamName) {

        this.teamsName = newTeamName;
    }

    public List<String> getUsers() {
        return users;
    }


}

