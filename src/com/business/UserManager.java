package com.business;

import com.persistent.*;
import com.presentation.LoginView;
import com.presentation.MainUserInterface;

import java.io.*;
import java.util.*;

/***
 Singleton class:
 Managing users documentation, responsible for creating, editing and removing users
 ***/

public class UserManager {

    private static UserManager UserManagerInstance; //Singleton instance

    private static String fileAddress = "src/com/data/usersFile.ser";
    public User loggedInUser;
    public HashMap<String, User> users;

    public static UserManager getInstance() {
        if (UserManagerInstance == null)
            UserManagerInstance = new UserManager();
        return  UserManagerInstance;
    }

    private UserManager() {
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

    public int addUser(String username, String password, User.PermissionLevel permission, String teamName)
        /*function for add user if the user have permission or is admin user
          return: 1- user created
                  2- Action no permitted
                  3- user not created
         */
    {
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

    private boolean addNewUser(String username, String password, User.PermissionLevel permission, String teamName)
        //function for add user if the user not exist in system
    {
        //check if user not exist in system
        if (!(isUserExist(username))) {
            //create user object
            User newUser = new User(username, password, permission,teamName);
            //insert new user to team list
            //TODO: Changed by TAMAR
            LoginView.teamManager.addMemberToTeam(username, LoginView.teamManager.teams.get(teamName));
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

    public int removeUser(String username)
        /*removeUser
          return: 1- user removed
                 2- Action no permitted
                3- Invalid to edit admin user
                4- user can't remove himself
         */
    {
        //check permission
        if (isActionPermitted()) {
            // user admin can't be removed
            if (!(username.equals("admin"))) {
                //user can't remove himself
                if (!(username.equals(loggedInUser.getUserName()))) {
                    //remove user from the team list
                    LoginView.teamManager.removeMemberFromTeam(username, LoginView.teamManager.teams.get(users.get(username).getTeamName()));
                    //change the owner of all WI under the username to Unassigned
                    for (Map.Entry<Integer, WorkItem> entry : MainUserInterface.WIManager.workItems.entrySet()) //adding rows
                        if (entry.getValue().getOwner() != null && entry.getValue().getOwner().equals(users.get(username).getUserName()))
                            entry.getValue().setOwner("Unassigned");
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


    public int updateUserPermission(String username, User.PermissionLevel newPermission)
        /*updateUserPermission
          return: 1- user updated
                  2- Action no permitted
                  3- Invalid to edit admin user
          */
    {
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


    public int updateUserTeam(String username, String newTeamName)
        /*updateUserTeam
          return: 1- user updated
                  2- Action no permitted
                  3- Invalid to edit admin user
         */
    {
        if (isActionPermitted()){
            if( !(username.equals("admin"))) {
                //remove the user from previous team
                LoginView.teamManager.removeMemberFromTeam(username, LoginView.teamManager.teams.get(users.get(username).getTeamName()));
                 //set user team to new team
                users.get(username).setTeam(newTeamName);
                 //insert the user to current list team
                //TODO:Changed by TAMAR
                LoginView.teamManager.addMemberToTeam(username, LoginView.teamManager.teams.get(users.get(username).getTeamName()));
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


    public Integer login(String username,String password)
        /*login
          return: 0- User not exist
                  1- login success
                  2- invalid password
         */
    {
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