package com.business;
import com.persistent.Team;
import com.persistent.User;
import com.presentation.MainUserInterface;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TeamManager {


    private static String fileAddress = "src/com/data/teamsFile.ser";
    public File teamsFile;
    public HashMap<String, Team> teams;



    public TeamManager ()
    {

        this.teams = new HashMap<String, Team>();
        HashMap<String,Team> map = new HashMap<>();

        try
        {

            FileInputStream fis = new FileInputStream(fileAddress);
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (HashMap) ois.readObject();
            teams.putAll(map);
            ois.close();
            fis.close();
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
            return;
        }catch(ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }
        System.out.println("Deserialized HashMap..");
        // Display content using Iterator
        Set set = map.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            System.out.print("key: "+ mentry.getKey() + " & Value: ");
            System.out.println(mentry.getValue());
        }


        if(teams.get("default")==null)//Default team
            teams.put("default", new Team("default"));


    }
    /**
     *The method read teamFile file.
     */
    public void readTeamsFile()
    {

    }


    public void addTeam(String teamName)
    {
       if(!isTeamExist(teamName))
           teams.put(teamName,new Team(teamName));

    }

    public Boolean removeTeam(String teamsName)
    {
        int size= this.teams.size();

        if(isTeamExist(teamsName))
            this.teams.remove(teamsName);

        return size==(this.teams.size()+1);
    }



    public Team getTeam(String teamName)
    {
        return this.teams.get(teamName);
    }
    public void addMemberToTeam(User user, Team team)
    {
           team.addUser(user);
    }

    public void removeMemberFromTeam(User user, Team team)
    {
        if(isUserBelongToTeam(team.getTeamsName(), user))
            team.removeUser(user);
    }

    public Boolean isTeamExist(String teamName)
    {

       Team tmp =  teams.get(teamName);
       return tmp!=null;



    }


    public Boolean isUserBelongToTeam(String teamName, User user)
    {
        if(!isTeamExist(teamName))
            return false;
        else
        {
            for(int i=0;i<teams.get(teamName).getUsers().size();i++)
            {
                if(teams.get(teamName).getUsers().get(i).getUserName()==user.getUserName())
                    return true;

            }
            return false;
        }
    }

    public Boolean isActionPermitted()
    {

        return true;
    }


    public void updateTeamsFile()
    {
        try
        {
            FileOutputStream fos =
                    new FileOutputStream(fileAddress);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(teams);
            oos.close();
            fos.close();
            System.out.printf("Serialized HashMap data is saved in teamsFile.ser");
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

    }



    public void updateTeamsName(String oldName, String newName)
    {
        teams.get(oldName).setTeamsName(newName);

    }

    public void printTeamManager()
    {

        for (Map.Entry<String, Team> stringTeamEntry : this.teams.entrySet()) {
            String teamsName = stringTeamEntry.getKey();
            Iterator<User> itUser = this.teams.get(teamsName).getUsersList().iterator();
            System.out.printf(teamsName + ":\n{");

            while (itUser.hasNext()) {
                System.out.printf(itUser.next().getUserName() + ",");
            }

            System.out.printf("}\n\n");
        }
    }
    public void printFile()
    {

    }



}
