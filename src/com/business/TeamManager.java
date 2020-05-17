package com.business;
import com.persistent.*;

import java.awt.font.TextHitInfo;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class TeamManager  {


    private File teamsFile;
    private HashMap<String, Team> teams;



    public TeamManager() throws IOException, Exception,FileNotFoundException{
        this.teams = new HashMap<>();
        this.teamsFile = new File("teamsFile.ser");
        try
        {
            FileOutputStream fos =
                    new FileOutputStream("teamsFile.ser");
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



    /**
     *The method read teamFile file.
     */
    public void readTeamsFile()
    {
        HashMap<Integer, String> map = null;
        try
        {
            FileInputStream fis = new FileInputStream("teamsFile.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (HashMap) ois.readObject();
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
    }

    /**
     *
     * @param teamName
     */
    public void addTeam(String teamName)
    {
       if(!isTeamExist(teamName))
           teams.put(teamName,new Team(teamName));
    }

    /**
     *The function would remove a team from the HashMap.
     * @param team The team we want to be removed
     * @return If the action was done by a user that isn't permitted to do the action(Not an admin) the function
     * would return false. Otherwise - it would return true.
     */
    public Boolean removeTeam(Team team)
    {
        if(isActionPermitted())
        {
            if(isTeamExist(team.getTeamsName()))
              teams.remove(team.getTeamsName());
        }
        else
            return false;

        return true;
    }

    /**
     *The function is adding a member to a certain team,only if the user is not part of another team.
     * @param user The user we want to add.
     * @param team The team we want to add the user to.
     */
    public void addMemberToTeam(User user, Team team)
    {
        //TODO:Integration with users list! Need to inform Ortal!
        /*Assumes the the user doesn't exist in the database for now!(there is a need to check it - throught the
        UserManager class. Maybe should be static?
         */

        if(isTeamExist(team.getTeamsName())) {

            team.addUser(user);
        }

    }

    /**
     *The function removes a member from a selected team.
     * @param user The user we want to delete
     * @param team The team we want to remove the user frmo
     */
    public void removeMemberFromTeam(User user, Team team)
    {
        if(isUserBelongToTeam(team.getTeamsName(), user))
            team.removeUser(user);
    }

    /**
     *The function checks if the given team is already exists in the teamManger hashMap. If it exists the function would
     * @param teamName
     * @return
     */
    public Boolean isTeamExist(String teamName)
    {
        return (teams.get(teamName)!=null);

    }


    /**
     *
     * @param teamName
     * @param user
     * @return
     */
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


    /**
     *
     * @return
     */
    public Boolean isActionPermitted()
    {

        return true;
    }


    /**
     *
     */
    public void updateTeamsFile() throws Exception{

        FileOutputStream fos = new FileOutputStream(teamsFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(teams);




    }


    /**======TEMP TEMP TEMP TEMP TEMP TEMP TEMP TEMP TEMP TEMP===========**/

   public void printTeams()
    {
        System.out.println(this.teams);
    }

    /**======TEMP TEMP TEMP TEMP TEMP TEMP TEMP TEMP TEMP TEMP===========**/





}
