import com.business.TeamManager;
import com.business.UserManager;
import com.persistent.Team;
import com.persistent.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class TeamManagerTest {
    TeamManager teamManager ;
    UserManager userManager;
    final String teamNameExample = "TestTeam";
    final String[] usernameExample = new String[]{"testUser1", "testUser2", "testUser3"};

    //d: System.out.println("Team Manager::  An attempt to edit a name to an existed one   [expected result: fail ]");

    @Before
    public void setUpMethod() {
        System.out.println("Set Up Method");
        userManager= UserManager.getInstance();
        userManager.login("admin","admin");
        teamManager = TeamManager.getInstance();
        teamManager.setFileAddress("src/com/data/teamsFileTest.ser");


    }

    @After
    public void tearDownMethod() {
        System.out.println("Tear Down Method");

        String testPath = "src/com/data/teamsFileTest.ser";
        File testFile = new File(testPath);
        if (testFile.exists())
            testFile.delete();

        teamManager = null;
    }

    //Editing a team's name to one that already exists.
    @Test
    public void testEditNewNameToAnExistOne() {
        System.out.println("Team Manager::  An attempt to edit a name to an existed one   [expected result: fail ]");

        teamManager.addTeam(teamNameExample);
        teamManager.addTeam("example");
        teamManager.updateTeamsName("example", teamNameExample);



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
        int teamSizeBefore = teamManager.teams.size();

        teamManager.removeTeam("NonExist");

        //Checking that another team wasn't removed by accident.
        Assert.assertEquals(teamSizeBefore,teamManager.teams.size());
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
        boolean isUserExist=false;
        teamManager.addTeam(teamNameExample);
        UserManager.getInstance().addUser("example-user","123",
                User.PermissionLevel.member,teamNameExample);

        teamManager.addMemberToTeam("example-user",teamManager.teams.get(teamNameExample));
        teamManager.addTeam("another-team");

        teamManager.addMemberToTeam("example-user",teamManager.teams.get("another-team"));

        Iterator<String> it  = teamManager.teams.get("another-team").getUsers().iterator();

        while(it.hasNext()&&!isUserExist)
        {
            if(it.next().equals("another-team"))
                isUserExist=true;
        }

        //Makes sure that the hashMap stays the same.
        Assert.assertFalse(isUserExist);
    }

    @Test
    public void testAddBlankUserToTeam()
    {

        boolean isBlankAdded = false;
        teamManager.addTeam(teamNameExample);
        teamManager.addMemberToTeam("", teamManager.teams.get(teamNameExample));

        Iterator<String> it = teamManager.teams.get(teamNameExample).getUsers().iterator();
        while(it.hasNext()&&!isBlankAdded)
        {
            if(it.next().equals(""))
                isBlankAdded=true;
        }
        Assert.assertFalse(isBlankAdded);
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
    public void testUpdateHashFromFile() throws IOException {
        System.out.println("Team Manager:: test update hash from file [expected result: success]");

        teamManager.addTeam(teamNameExample);
        File file = new File("src/com/data/teamsFileTest.ser");
        file.delete();

        teamManager.addTeam("example-team");
        teamManager.updateTeamsFile();
        teamManager.teams.clear();

        teamManager.loadTeamsFileToHashMap();
        Assert.assertTrue(teamManager.isTeamExist("example-team"));



    }

    //Update of the hash from creating a new object
    @Test
    public void testUpdateHasOnCreatingObject()
    {
        System.out.println("Team Manager:: test update has on creating object(And not through the teamManager). ([expected result: failure]");

        Team team = new Team("DontAddMe");

            Assert.assertFalse(teamManager.isTeamExist("DontAddMe"));
    }

    //Searching a team that doesn't exist in the hashMap
    @Test
    public void testSearchNonExistTeam()
    {
            Assert.assertNull(teamManager.getTeam("example"));
    }


    /*
    ### Other ###

       - Permission handling
            + Only admin account can modify the teams hashMap.
     */
    @Test public void testPermissionTeamModification()
    {
        teamManager.addTeam(teamNameExample);
        teamManager.addMemberToTeam("nonAdmin",teamManager.teams.get(teamNameExample));
        userManager.addUser("nonAdmin","123", User.PermissionLevel.member,teamNameExample);
        userManager.login("nonAdmin","123");//

    }



}
