package com.business;
import com.persistent.Team;
import com.persistent.User;
import java.io.File;
import java.util.HashMap;

public class TeamManager {


    private File teamsFile;
    private HashMap<String, Team> teams;



    public TeamManager()
    {
        //TODO: complete creation of the new file
        //TODO: Complete update of the file

        this.teams = new HashMap<>();
    }
    /**
     *The method read teamFile file.
     */
    public void readTeamsFile()
    {

    }

    /**
     *
     * @param teamName
     */
    public void addTeam(String teamName)
    {
       if(isTeamExist(teamName))
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
     *
     * @param user
     * @param team
     */
    public void removeMemberFromTeam(User user, Team team)
    {
        if(isUserBelongToTeam(team.getTeamsName(), user))
            team.removeUser(user);
    }

    /**
     *
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
    public void updateTeamsFile()
    {

    }





}
