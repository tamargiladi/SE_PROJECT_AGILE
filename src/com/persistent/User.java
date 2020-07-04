package com.persistent;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;
    private String password;
    private PermissionLevel permissionLevel;
    private String teamName;

    public enum PermissionLevel{
        member, manager, admin
    }

    public User(String userName, String password, PermissionLevel permissionLevel, String teamName) {
        this.userName = userName;
        this.password = password;
        this.permissionLevel = permissionLevel;
        this.teamName = teamName;
    }

    public User(String userName) {
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

    public String getTeamName(){
        return teamName;
    }

    public void setPermissionLevel(PermissionLevel permissionLevel){
        this.permissionLevel = permissionLevel;
    }

    public void setTeam(String teamName){
        this.teamName = teamName;
    }
}
