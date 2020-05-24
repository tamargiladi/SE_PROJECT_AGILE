package com.persistent;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    final private String userName;
    final private String password;
    private PermissionLevel permissionLevel;
    private Team team;

    public enum PermissionLevel{
        member, manager, admin
    }

    public User(String userName, String password, PermissionLevel permissionLevel, Team team) {
        this.userName = userName;
        this.password = password;
        this.permissionLevel = permissionLevel;
        this.team = team;
    }

    public User(String userName)
    {
        this.userName=userName;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword(){
        return password;
    }

    public PermissionLevel getPermissionLevel(){
        return permissionLevel;
    }

    public Team getTeam(){
        return team;
    }

    public void setPermissionLevel(PermissionLevel permissionLevel){
        this.permissionLevel = permissionLevel;
    }

    public void setTeam(Team team){
        this.team = team;
    }
}
