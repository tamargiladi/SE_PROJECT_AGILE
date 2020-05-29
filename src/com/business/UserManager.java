package com.business;

import com.persistent.*;
import com.presentation.LoginView;

import java.io.*;
import java.util.*;


public class UserManager {

    private static String fileAddress = "src/com/data/usersFile.ser";
    public User loggedInUser;
    public HashMap<String, User> users;

    public UserManager() {
        try {
            users = new HashMap<>();
            loadUsersFileToHashMap();

            //check if  admin user exist, if not crete this user
            /*An admin user must exist in the system to ensure
              that there is at least one user with all permissions */
            if(!(isUserExist("admin"))){
                /*for adding a user must be a team
                  create default team */
                LoginView.teamManager.addTeam("default");
                addUser("admin","admin", User.PermissionLevel.admin,"default");
            }

        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("failed to create UserManager");
        }
    }

    //function for add user if the user have permission or is admin user
    public int addUser(String username, String password, User.PermissionLevel permission, String teamName) {
        //create user admin when the system run in the first time
        if (loggedInUser==null && username.equals("admin"))
            if (addNewUser(username,password, permission,teamName))
                return 1;

        //add new user after login
        if (isActionPermitted()) {
            if (addNewUser(username, password, permission, teamName))
                return 1;
            else  return 3;
        }
        // Action no permitted
        System.out.println("Action no permitted\n");
        return 2;
    }

    //function for add user if the user not exist in system
    private boolean addNewUser(String username, String password, User.PermissionLevel permission, String teamName){
        //check if user not exist in system
        if (!(isUserExist(username))) {
            //create user object
            User newUser = new User(username, password, permission,LoginView.teamManager.teams.get(teamName));
            //insert new user to team list
            LoginView.teamManager.addMemberToTeam(newUser, newUser.getTeam());
            //insert new user to users HashMap
            users.put(username, newUser);
            //update the user file with new user
            updateUsersFile();
            return true;
        }
        return false;
    }

    public Boolean isUserExist(String username) {
        return users.containsKey(username);
    }

    public int removeUser(String username) {
        //check permission
        if (isActionPermitted()) {
            // user admin can't be removed
            if (!(username.equals("admin"))) {
                //user can't remove himself
                if (!(username.equals(loggedInUser.getUserName()))) {
                    //remove user from the team list
                    LoginView.teamManager.removeMemberFromTeam(users.get(username), users.get(username).getTeam());
                    //remove the user
                    //TODO: check if u User remove
                    User u = users.get(username);
                    u = null;
                    //remove user from users HashMap
                    users.remove(username, users.get(username));
                    //update file
                    updateUsersFile();
                    System.out.println("user removed\n");
                    return 1;
                }
                //user can't remove himself
                else {
                    System.out.println("user can't remove himself\n");
                    return 4;
                }
            }
            // user admin can't be removed
            else {
                System.out.println("Invalid to edit admin user\n");
                return 3;
            }
        }
        //check permission
        else {
            System.out.println("Action no permitted\n");
            return 2;
        }
    }

    public int updateUserPermission(String username, User.PermissionLevel newPermission) {
        if (isActionPermitted()){
            if (!(username.equals("admin"))){
                users.get(username).setPermissionLevel(newPermission);
                updateUsersFile();
                return 1;
            }
            else{
                System.out.println("Invalid to edit admin user\n");
                return 3;
            }
        }
        else System.out.println("Action no permitted\n");
        return 2;
    }

    public int updateUserTeam(String username, String newTeamName) {
        if (isActionPermitted()){
            if( !(username.equals("admin"))) {
                //remove the user from previous team
                LoginView.teamManager.removeMemberFromTeam(users.get(username), users.get(username).getTeam());
                 //set user team to new team
                users.get(username).setTeam(LoginView.teamManager.teams.get(newTeamName));
                 //insert the user to current list team
                LoginView.teamManager.addMemberToTeam(users.get(username), users.get(username).getTeam());
                updateUsersFile();
                return 1;
             }
            else{
                System.out.println("Invalid to edit admin user\n");
                return 3;
            }
        }
        else System.out.println("Action no permitted\n");
        return 2;
    }

    public Integer login(String username,String password){
        if (isUserExist(username))
            if ((users.get(username).getPassword().equals(password))) {
                this.loggedInUser = users.get(username);
                return 1;
            }
            else{
                  System.out.println("invalid password\n");
                  return 2;
                }

         System.out.println("User not exist\n");
         return 0;
    }

    public Boolean isActionPermitted(){
        return (this.loggedInUser.getPermissionLevel()==User.PermissionLevel.manager || this.loggedInUser.getPermissionLevel()==User.PermissionLevel.admin);
    }

    public void loadUsersFileToHashMap() {
        File usersFile = new File(fileAddress);
        //if the not file empty,need to load users file to HashMap
        if (usersFile.length() != 0){
            // load users file to HashMap
          try {
                FileInputStream userFileInputStream = new FileInputStream(fileAddress);
                ObjectInputStream userObjectInputStream = new ObjectInputStream(userFileInputStream);
                User u = (User) userObjectInputStream.readObject();
                //TODO: in last iter have IOException
                while (u != null) {
                    users.put(u.getUserName(),u);
                    u = (User) userObjectInputStream.readObject();
                }
                userObjectInputStream.close();
                userFileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                //System.out.println("failed to load users file to HashMap\n");
            }catch (ClassNotFoundException c) {
                    c.printStackTrace();
                    System.out.println("Class not found");
            }
        }
    }

    public void updateUsersFile(){
        try {
            FileOutputStream userFileOutputStream = new FileOutputStream(fileAddress);
            ObjectOutputStream userObjectOutputStream = new ObjectOutputStream(userFileOutputStream);
            for (Map.Entry<String, User> entry : users.entrySet())
                userObjectOutputStream.writeObject(entry.getValue());
            userObjectOutputStream.close();
            userFileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("failed to update user file from HashMap\n");
        }
    }
}