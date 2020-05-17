package com.business;
import com.persistent.User;
import com.persistent.Team;
import java.io.File;
import java.util.HashMap;


public class UserManager {

    private File usersFile;
    private User loggedInUser;
    private HashMap <String, User> users;

    public UserManager() {
        this.users = new HashMap<>();
    }

    public void readUsersFile(){}

    public void addUser(String username, String password, User.PermissionLevel permission, Team team){
        //TODO: TRY-addUser
        if (isActionPermitted())
            if(!(isUserExist(username))){
                User newUser=new User(username,password,permission,team);
                users.put(username,newUser);
             }
    }

    public Boolean isUserExist(String username) {
        return users.containsKey(username);
    }

    public void removeUser(User user){
        //TODO: TRY - removeUser
        //TODO: the object removed?
        if (isActionPermitted())
            users.remove(user.getUserName(),user);

    }

    public void updateUserPermission(User user, User.PermissionLevel newPermission){
        if (isActionPermitted())
            user.setPermissionLevel(newPermission);
    }

    public void updateUserTeam(User user,Team newTeam){
        if (isActionPermitted())
            user.setTeam(newTeam);
    }

    public Boolean login(String username,String password){
        if (isUserExist(username))
            if ((users.get(username).getPassword().equals(password))){
                this.loggedInUser = users.get(username);
                return true;
            }
         return false;
    }

    public Boolean isActionPermitted(){
        return (this.loggedInUser.getPermissionLevel().equals(User.PermissionLevel.manager) || this.loggedInUser.getPermissionLevel().equals(User.PermissionLevel.admin));
    }

    public void updateUsersFile(){

    }
}