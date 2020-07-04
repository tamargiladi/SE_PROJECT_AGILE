package com.business;

import com.persistent.Team;
import com.persistent.User;
import com.persistent.WorkItem;
import com.presentation.LoginView;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TeamManager {

    private static TeamManager TeamManagerInstance; //Singleton instance

    private static String fileAddress = "src/com/data/teamsFile.ser";
    public HashMap<String, Team> teams;//


    public static TeamManager getInstance() {
        if (TeamManagerInstance == null)
            TeamManagerInstance = new TeamManager();

        return TeamManagerInstance;
    }

    private TeamManager() {

        this.teams = new HashMap<String, Team>();

        loadTeamsFileToHashMap();

    }


    public void addTeam(String teamName) {
        if ((teamName.equals("default")||teamName.equals("Algo")||teamName.equals("SW"))||(!isTeamExist(teamName)&&isAdmin()))


            teams.put(teamName, new Team(teamName));

    }

    public void removeTeam(String teamsName) {

        if (isTeamExist(teamsName) && teams.get(teamsName).getUsers().size() == 0 && !teamsName.equals("default")&&isAdmin())
            this.teams.remove(teamsName);
    }

    public Team getTeam(String teamName) {

        if (isTeamExist(teamName))
            return this.teams.get(teamName);
        else
            return null;
    }

    public void addMemberToTeam(String username, Team team) {

        if (!username.isEmpty())
            team.addUser(username);
    }

    public void removeMemberFromTeam(String username, Team team) {
        if (isUserBelongToTeam(team.getTeamsName(), username)&&isAdmin())
            team.removeUser(username);
    }

    public Boolean isTeamExist(String teamName) {
        Team tmp = teams.get(teamName);
        return tmp != null;
    }

    public Boolean isUserBelongToTeam(String teamName, String username) {
        if (!isTeamExist(teamName))
            return false;
        else {
            for (int i = 0; i < teams.get(teamName).getUsers().size(); i++) {
                if (teams.get(teamName).getUsers().get(i).equals(username))
                    return true;

            }
            return false;
        }
    }

    public Boolean updateTeamsFile() {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileAddress);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(teams);
            oos.close();
            fos.close();
            System.out.printf("Serialized HashMap data is saved in teamsFile.ser");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return (fos != null);
    }


    public void updateTeamsName(String oldName, String newName) {
        Team team = teams.get(oldName);
        team.setTeamsName(newName);


        if (!isTeamExist(newName)&&isAdmin()) {
            teams.remove(oldName);
            teams.put(newName, team);
            // Change team name for all users belongs to that team
            for (Map.Entry<String, User> stringUserEntry : LoginView.userManager.users.entrySet()) {
                String username = stringUserEntry.getKey();
                String userTeam = stringUserEntry.getValue().getTeamName();
                if (userTeam.equals(oldName))
                    LoginView.userManager.updateUserTeam(username, newName);
            }

            //Change team name for all work items associated with that team
            for (Map.Entry<Integer, WorkItem> workItemEntry : WorkItemManager.getInstance().workItems.entrySet()) {
                Integer id = workItemEntry.getKey();
                String teamName = workItemEntry.getValue().getTeam();
                if (teamName != null && teamName.equals(oldName))
                    workItemEntry.getValue().setTeam(newName);
            }

        }

    }

    public void setFileAddress(String newAddress) {
        fileAddress = newAddress;
    }

    public void loadTeamsFileToHashMap() {
        HashMap<String, Team> map = new HashMap<>();

        File teamsFile = new File(fileAddress);
        if (teamsFile.length() != 0) {
            // load users file to HashMap
            try {
                FileInputStream teamFileInputStream = new FileInputStream(fileAddress);
                ObjectInputStream teamObjectInputStream = new ObjectInputStream(teamFileInputStream);
                map = (HashMap) teamObjectInputStream.readObject();
                teams.putAll(map);
                teamObjectInputStream.close();
                teamFileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException c) {
                c.printStackTrace();
                System.out.println("Class not found");
            }
        }

        if (teams.size() <= 1)//Default team
        {

            if (!isTeamExist("default"))
                addTeam("default");
            addTeam("Algo");
            addTeam("SW");
        }
    }

    public  boolean isAdmin() {
        return UserManager.getInstance().loggedInUser.getPermissionLevel() == User.PermissionLevel.admin;
    }
}