import com.business.TeamManager;
import com.business.WorkItemManager;
import com.persistent.Team;


import com.persistent.WorkItem;
import com.presentation.LoginView;
import org.junit.*;

import java.io.*;
import java.lang.*;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Random;

public class TeamManagerTest {
    TeamManager teamManager ;
    final String teamNameExample = "TestTeam";
    final String[] usernameExample = new String[]{"testUser1", "testUser2", "testUser3"};

    //d: System.out.println("Team Manager::  An attempt to edit a name to an existed one   [expected result: fail ]");

    @Before
    public void setUpMethod() {
        System.out.println("Set Up Method");
        teamManager = TeamManager.getInstance();
    }

    @After
    public void tearDownMethod() {
        System.out.println("Tear Down Method");
        teamManager = null;
    }

    //Editing a team's name to one that already exists.
    @Test
    public void testEditNewNameToAnExistOne() {
        System.out.println("Team Manager::  An attempt to edit a name to an existed one   [expected result: fail ]");


        TeamManager beforeManager = TeamManager.getInstance();//To compare the 'wrong' hashMap.


        teamManager.addTeam(teamNameExample);
        beforeManager.addTeam(teamNameExample);
        teamManager.addTeam(teamNameExample);//Adds again to the test teamManager.

        Assert.assertSame(beforeManager.teams, teamManager.teams);

    }

    // Entering a blank name.
    @Test
    public void testEnteringBlankName()
    {
        System.out.println("Team Manager::  An attempt to edit a name to a blank one [expected result: fail ]");

        teamManager.addTeam(teamNameExample);
        teamManager.teams.get(teamNameExample).setTeamsName("");

        Assert.assertFalse(teamManager.isTeamExist(""));
    }



    //### Removing a team ###

    //An attempt to remove a team that has users within.
    @Test
    public void testRemoveNonEmptyTeam()
    {
        System.out.println("Team Manager::  An attempt to remove a team that has users within it.  [ expected result: fail ]");

        teamManager.addTeam(teamNameExample);
        teamManager.addMemberToTeam(usernameExample[0], teamManager.teams.get(teamNameExample));
        teamManager.removeTeam(teamNameExample);

        Assert.assertTrue(teamManager.isTeamExist(teamNameExample));

    }

    // An attempt to remove a team that doesn't exist
    @Test
    public void testRemoveNonExistentTeam()
    {
        System.out.println("Team Manager::  An attempt to remove a team that doesn't exist.  [ expected result: fail ]");

        TeamManager testTeam = TeamManager.getInstance();

        teamManager.addTeam(teamNameExample);
        testTeam.addTeam(teamNameExample);

        teamManager.removeTeam("NonExist");

        //Checking that another team wasn't removed by accident.
        Assert.assertEquals(teamManager.teams, testTeam.teams);
    }

    // Block the permission to remove 'default' team.
    @Test
    public void testRemoveDefaultTeam()
    {
        System.out.println("Team Manager::  An attempt to remove the team 'default' .  [ expected result: fail ]");
        teamManager.addTeam("default");
        teamManager.removeTeam("default");

        Assert.assertTrue(teamManager.isTeamExist("default"));


    }


    //### Adding/edit/remove users from a team ###

    // Adding a member that already exists to a team..
    @Test
    public void testAddExistedMemberToTeam()
    {
        TeamManager testTeam = TeamManager.getInstance();

        teamManager.addTeam(teamNameExample);
        testTeam.addTeam(teamNameExample);

        teamManager.addMemberToTeam(usernameExample[0],teamManager.teams.get(teamNameExample));
        testTeam.addMemberToTeam(usernameExample[0],testTeam.teams.get(teamNameExample));

        teamManager.removeMemberFromTeam(usernameExample[1],teamManager.teams.get(teamNameExample));

        //Makes sure that the hashMap stays the same.
        Assert.assertEquals(teamManager.teams, testTeam.teams);
    }

    @Test
    public void testAddBlankUserToTeam()
    {
        teamManager.addTeam(teamNameExample);
        teamManager.addMemberToTeam("", teamManager.teams.get(teamNameExample));

        Assert.assertFalse(teamManager.isTeamExist(""));
    }

    //### File handling ###

    // Synchronization of the file & hashMap variable
    @Test
    public void testSyncFileHashMap()
    {
        /* Steps:
           1. Add the new team to 'teamManager'
           2. Creation of a new example file to test the synchronization
           3. Add another team to 'teamManager'
           4. Pull the file from 'teamManager'
           5. Assert that the pulled file is indeed different from the previously saved file.
         */

        System.out.println("Team Manager:: Save teams to team's file [expected result: success]");
        String testPath = "src/com/data/teamsFileTest.ser";
        teamManager.setFileAddress(testPath);
        teamManager.addTeam(teamNameExample);
        Assert.assertTrue(teamManager.updateTeamsFile());
    }

    // Write to file
    @Test
    public void testLoadTeamsToFile() throws IOException {
        System.out.println("Team Manager:: Load teams to team's file [expected result: success]");
        String testPath = "src/com/data/teamsFileTest.ser";
        teamManager.setFileAddress(testPath);
        teamManager.addTeam(teamNameExample);
        teamManager.updateTeamsFile();
        teamManager.teams.clear();
        teamManager.loadTeamsFileToHashMap();

        Assert.assertTrue(teamManager.teams.containsKey(teamNameExample));
    }

    // read from file + correctness
    @Test
    public void testLoadTeamsUsersToFile() throws IOException {
        System.out.println("Team Manager:: Load teams to team's file [expected result: success]");
        String testPath = "src/com/data/teamsFileTest.ser";
        teamManager.setFileAddress(testPath);
        teamManager.addTeam(teamNameExample);
        teamManager.addMemberToTeam(usernameExample[0],teamManager.teams.get(teamNameExample));
        teamManager.updateTeamsFile();
        teamManager.teams.clear();
        teamManager.loadTeamsFileToHashMap();

        Assert.assertTrue(teamManager.isUserBelongToTeam(teamNameExample,usernameExample[0]));
    }





    //### Hash Handling ###
    //Update hash from a file.
    @Test
    public void testUpdateHashFromFile()
    {
    }

    //Update of the hash from creating a new object
    @Test
    public void testUpdateHasOnCreatingObject()
    {

    }

    //Searching a team that doesn't exist in the hashMap //But I do this 😅
    @Test
    public void testSearchNonExistTeam()
    {}


    /*
    ### Other ###

       - Permission handling
            + Only admin account can modify the teams hashMap.
     */
    @Test public void testPermissionTeamModification()
    {

        //TODO: [Q] -> should I use the UI in order to check permissions?

     /*   teamManager.addTeam(teamNameExample);

        Assert.assertTrue(LoginView.userManager.loggedInUser.getPermissionLevel().equals("admin") && teamManager.teams.size()==1||
                teamManager.teams.size()==0);*/
    }


    @AfterClass
    public static void deleteTestFile() {
        String testPath = "src/com/data/teamsFileTest.ser";
        File testFile = new File(testPath);
        if (testFile.exists())
            testFile.delete();
    }




}
