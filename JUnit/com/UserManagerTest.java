import com.business.TeamManager;
import com.business.UserManager;
import com.business.WorkItemManager;
import com.persistent.User;
import com.persistent.WorkItem;
import org.junit.*;

import java.io.File;

public class UserManagerTest {

    UserManager userManager;
    private static String userTestPath = "src/com/data/usersTest.ser";
    private File testFile;

    @Before
    public void setUpMethod() {
        System.out.println("Set Up Method");
        testFile = new File(userTestPath);
        UserManager.setUserFileAddress(userTestPath);
        userManager = UserManager.getInstance();

        userManager.login("admin", "admin");
        userManager.addUser("demo-user","test", User.PermissionLevel.admin,"default");
    }

    @After
    public void tearDownMethod() {
        System.out.println("Tear Down Method");

        userManager.users.clear();
        userManager.updateUsersFile();

        if (testFile.exists())
            if (testFile.delete())
                 System.out.println("Deleted the file: " + testFile.getName());

        userManager = null;
    }

    // --- CHECK admin user is created in  user manager constructor---
    @Test
    public void testAdminUserExistAfterUserManagerConstructor() {
        System.out.println("User Manager:: admin user is created in  user manager constructor [ expected result: success ]");

        Assert.assertTrue(userManager.isUserExist("admin"));
    }
    // --- END admin user created in  user manager constructor ---


    //==========================================================================================
    //                                  Add User Testing
    //==========================================================================================

    // --- CHECK permission levels for add user---
    @Test
    public void testAddUserPermissionAdmin() {
        System.out.println("User Manager:: permission test for add user - admin   [ expected result: permitted ]");

        userManager.login("demo-user", "test");
        userManager.addUser("demo-user2","test",User.PermissionLevel.admin,"default");

        Assert.assertTrue(userManager.isUserExist("demo-user2"));
    }
    @Test
    public void testAddUserPermissionManager() {
        System.out.println("User Manager:: permission test for add user - manager  [ expected result: permitted ]");

        userManager.login("demo-user", "test");
        userManager.updateUserPermission("demo-user",User.PermissionLevel.manager);
        userManager.addUser("demo-user2","test",User.PermissionLevel.admin,"default");

        Assert.assertTrue(userManager.isUserExist("demo-user2"));
    }
    @Test
    public void testAddUserPermissionMember() {
        System.out.println("User Manager:: permission test for add user - member  [ expected result: not permitted ]");

        userManager.login("demo-user", "test");
        userManager.updateUserPermission("demo-user",User.PermissionLevel.member);
        userManager.addUser("demo-user2","test",User.PermissionLevel.admin,"default");

        Assert.assertFalse(userManager.isUserExist("demo-user2"));
    }
    // --- END permission levels for add user ---

    // --- CHECK adding existing user---
    @Test
    public void testAddExistUser() {
        System.out.println("User Manager:: adding existing user  [ expected result: fail ]");

        userManager.login("demo-user", "test");

        Assert.assertFalse(userManager.addUser("demo-user","test",User.PermissionLevel.admin,"default"));
    }
    // --- END adding existing user---

    // --- CHECK adding user with valid inputs---
    @Test
    public void testAddUserValidInputs() {
        System.out.println("User Manager:: adding user to the users HashMap [ expected result: success ]");

        userManager.login("demo-user", "test");
        userManager.addUser("demo-user2","test",User.PermissionLevel.admin,"default");

        Assert.assertTrue(userManager.isUserExist("demo-user2"));
    }
    @Test
    public void testAddMemberToTeam() {
        System.out.println("User Manager:: adding user to team list [ expected result: success ]");

        Assert.assertTrue(TeamManager.getInstance().isUserBelongToTeam("default","demo-user"));
    }
    // --- END check add user for valid inputs---

    // --- CHECK adding user with invalid  inputs---
    @Test
    public void testAddUserInvalidUsername() {
        System.out.println("User Manager:: adding user with null username [ expected result: fail ]");

        userManager.login("demo-user", "test");
        userManager.addUser(null,"test",User.PermissionLevel.admin,"default");

        Assert.assertFalse(UserManager.getInstance().isUserExist(null));
    }
    @Test
    public void testAddUserInvalidPassword() {
        System.out.println("User Manager:: adding user with null password [ expected result: fail ]");

        userManager.login("demo-user", "test");
        userManager.addUser("demo-user2",null,User.PermissionLevel.admin,"default");

        Assert.assertFalse(UserManager.getInstance().isUserExist("demo-user2"));
    }
    @Test
    public void testAddUserInvalidTeamName() {
        System.out.println("User Manager:: adding user with non exist team [ expected result: fail ]");

        userManager.login("demo-user", "test");
        userManager.addUser("demo-user2","test",User.PermissionLevel.admin,"test");

        Assert.assertFalse(UserManager.getInstance().isUserExist("demo-user2"));
    }
    // --- END check add user for invalid inputs---

    // --- CHECK load users file to HashMap after adding user---
    @Test
    public void testAddUserLoadUsersFileToHashMap() {
        System.out.println("User Manager:: add user to users file [ expected result: success ]");

        userManager.login("demo-user", "test");
        userManager.addUser("demo-user2","test",User.PermissionLevel.admin,"default");
        UserManager.getInstance().users.clear();
        UserManager.getInstance().loadUsersFileToHashMap();

        Assert.assertTrue(UserManager.getInstance().isUserExist("demo-user2"));
    }
    // --- END check load users file to HashMap after adding user---

    //==========================================================================================
    //                                    End Add User Testing
    //==========================================================================================


    // --- CHECK the function "isUserExist" for non exist user---
    @Test
    public void testIsUserExist() {
        System.out.println("User Manager:: isUserExist for non exist user [ expected result: fail ]");

        Assert.assertFalse(UserManager.getInstance().isUserExist("demo-user2"));
    }
    // --- END check the function "isUserExist" for non exist user---


    //==========================================================================================
    //                                    Remove User Testing
    //==========================================================================================

    // --- CHECK permission levels for remove user---
    @Test
    public void testRemoveUserPermissionAdmin() {
        System.out.println("User Manager:: permission test for remove user - admin   [ expected result: permitted ]");

        userManager.login("demo-user", "test");
        userManager.addUser("demo-user2","test",User.PermissionLevel.admin,"default");
        userManager.removeUser("demo-user2");

        Assert.assertFalse(userManager.isUserExist("demo-user2"));
    }
    @Test
    public void testRemoveUserPermissionManager() {
        System.out.println("User Manager:: permission test for remove user - manager  [ expected result: permitted ]");

        userManager.login("demo-user", "test");
        userManager.updateUserPermission("demo-user",User.PermissionLevel.manager);
        userManager.addUser("demo-user2","test",User.PermissionLevel.admin,"default");
        userManager.removeUser("demo-user2");

        Assert.assertFalse(userManager.isUserExist("demo-user2"));
    }
    @Test
    public void testRemoveUserPermissionMember() {
        System.out.println("User Manager:: permission test for remove user - member  [ expected result: not permitted ]");

        userManager.login("demo-user", "test");
        userManager.addUser("demo-user2","test",User.PermissionLevel.admin,"default");
        userManager.updateUserPermission("demo-user",User.PermissionLevel.member);
        userManager.removeUser("demo-user2");

        Assert.assertTrue(userManager.isUserExist("demo-user2"));
    }
    // --- END permission levels for remove user ---

    // --- CHECK remove admin user---
    @Test
    public void testRemoveAdminUser() {
        System.out.println("User Manager:: remove admin user [ expected result: fail ]");

        userManager.login("demo-user", "test");
        userManager.removeUser("admin");

        Assert.assertTrue(userManager.isUserExist("admin"));
    }
    // --- END remove admin user ---

    // --- CHECK remove login user---
    @Test
    public void testRemoveLoginUser() {
        System.out.println("User Manager:: remove login user [ expected result: fail ]");

        userManager.login("demo-user", "test");
        userManager.removeUser("demo-user");

        Assert.assertTrue(userManager.isUserExist("demo-user"));
    }
    // --- END remove login user ---

    // --- CHECK removing user---
    @Test
    public void testRemoveUser() {
        System.out.println("User Manager:: removing user from the users HashMap [ expected result: success ]");

        userManager.removeUser("demo-user");

        Assert.assertFalse(userManager.isUserExist("demo-user"));
    }
    @Test
    public void testRemoveMemberFromTeam() {
        System.out.println("User Manager:: remove user from team list [ expected result: success ]");

        userManager.removeUser("demo-user");

        Assert.assertFalse(TeamManager.getInstance().isUserBelongToTeam("default","demo-user"));
    }
    @Test
    public void testChangeOwnerFromAllWI() {
        System.out.println("User Manager:: change the owner of all WI under the username to Unassigned [ expected result: success ]");

       WorkItem wi=WorkItemManager.getInstance().createNewWorkItem().withSummary("demo").withDescription("demo test").withOwner("demo-user").build(WorkItem.typeEnum.Task,null);
        userManager.removeUser("demo-user");

        Assert.assertEquals(WorkItemManager.getInstance().workItems.get(wi.getId()).getOwner(),"Unassigned");
    }
    // --- End check removing user---

    // --- CHECK load users file to HashMap after removing user---
    @Test
    public void testRemoveUser_LoadUsersFileToHashMap() {
        System.out.println("User Manager:: remove user from users file [ expected result: success ]");

        userManager.removeUser("demo-user");
        UserManager.getInstance().users.clear();
        UserManager.getInstance().loadUsersFileToHashMap();

        Assert.assertFalse(UserManager.getInstance().isUserExist("demo-user"));
    }
    // --- END check load users file to HashMap after removing user---

    //==========================================================================================
    //                              End  Remove User Testing
    //==========================================================================================

    //==========================================================================================
    //                             Update User Permission Testing
    //==========================================================================================
    // --- CHECK permission for update user permission---
    @Test
    public void testUpdateUserPermissionAdmin() {
        System.out.println("User Manager:: permission test for update user permission  - admin   [ expected result: permitted ]");

        userManager.updateUserPermission("demo-user",User.PermissionLevel.admin);
        userManager.login("demo-user","test");

        Assert.assertTrue(userManager.isActionPermitted());
    }
    @Test
    public void testUpdateUserPermissionManager() {
        System.out.println("User Manager:: permission test for update user permission - manager  [ expected result: permitted ]");

        userManager.updateUserPermission("demo-user",User.PermissionLevel.manager);
        userManager.login("demo-user","test");

        Assert.assertTrue(userManager.isActionPermitted());
    }
    @Test
    public void testUpdateUserPermissionMember() {
        System.out.println("User Manager:: permission test for update user permission - member  [ expected result: not permitted ]");

        userManager.updateUserPermission("demo-user",User.PermissionLevel.member);
        userManager.login("demo-user","test");

        Assert.assertFalse(userManager.isActionPermitted());
    }
    // --- END permission permission for update user permission ---

    // --- CHECK update admin permission ---
    @Test
    public void testUpdateAdminPermission() {
        System.out.println("User Manager:: update admin permission [ expected result: fail ]");

        userManager.login("demo-user", "test");
        userManager.updateUserPermission("admin",User.PermissionLevel.member);
        userManager.login("admin","admin");

        Assert.assertTrue(userManager.isActionPermitted());
    }
    // --- END update admin permission ---

    // --- CHECK update permission for login user---
    @Test
    public void testUpdateLoginUserPermissionManager() {
        System.out.println("User Manager:: update permission for login user - manager[ expected result: success ]");

        userManager.login("demo-user", "test");
        userManager.updateUserPermission("demo-user",User.PermissionLevel.manager);

        Assert.assertTrue(userManager.isActionPermitted());
    }
    @Test
    public void testUpdateLoginUserPermissionMember() {
        System.out.println("User Manager:: update permission for login user - member [ expected result: success ]");

        userManager.login("demo-user", "test");
        userManager.updateUserPermission("demo-user",User.PermissionLevel.member);

        Assert.assertFalse(userManager.isActionPermitted());
    }
    // --- END check update permission for login user ---

    //==========================================================================================
    //                            End Update User Permission Testing
    //==========================================================================================

    //==========================================================================================
    //                                 Update User Team Testing
    //==========================================================================================
    // --- CHECK permission for update user team---
    @Test
    public void testUpdateUserTeamPermissionAdmin() {
        System.out.println("User Manager:: permission test for update user team  - admin   [ expected result: permitted ]");

        userManager.updateUserTeam("demo-user","SW");

        Assert.assertTrue(TeamManager.getInstance().isUserBelongToTeam("SW","demo-user"));
    }
    @Test
    public void testUpdateUserTeamPermissionManager() {
        System.out.println("User Manager:: permission test for update user team - manager  [ expected result: permitted ]");

        userManager.login("demo-user","test");
        userManager.updateUserPermission("demo-user",User.PermissionLevel.manager);
        userManager.updateUserTeam("demo-user","SW");

        Assert.assertTrue(TeamManager.getInstance().isUserBelongToTeam("SW","demo-user"));
    }
    @Test
    public void testUpdateUserTeamPermissionMember() {
        System.out.println("User Manager:: permission test for update user team - member  [ expected result: not permitted ]");

        userManager.login("demo-user","test");
        userManager.updateUserPermission("demo-user",User.PermissionLevel.member);
        userManager.updateUserTeam("demo-user","SW");

        Assert.assertFalse(TeamManager.getInstance().isUserBelongToTeam("SW","demo-user"));
    }
    // --- END permission for update user team ---

    // --- CHECK update admin team ---
    @Test
    public void testUpdateAdminTeam() {
        System.out.println("User Manager:: update admin team [ expected result: fail ]");

        userManager.updateUserTeam("admin","SW");

        Assert.assertFalse(TeamManager.getInstance().isUserBelongToTeam("SW","admin"));
    }
    // --- END update admin team ---

    // --- CHECK update user team to non exist team---
    @Test
    public void testUpdateToNonExistTeam() {
        System.out.println("User Manager:: update user team to non exist team [ expected result: fail ]");

        userManager.updateUserTeam("demo-test","test");

        Assert.assertFalse(TeamManager.getInstance().isUserBelongToTeam("test","demo-test"));
    }
    // --- END update user team to non exist team ---

    // --- CHECK update user team ---
    @Test
    public void testRemoveUserFromPreviousTeam() {
        System.out.println("User Manager:: remove user from previous team [ expected result: success ]");

        userManager.updateUserTeam("demo-user","SW");

        Assert.assertFalse(TeamManager.getInstance().isUserBelongToTeam("default","demo-user"));
    }
    @Test
    public void testInsertUserToNewTeam() {
        System.out.println("User Manager:: insert user to new team [ expected result: success ]");

        userManager.updateUserTeam("demo-user","SW");

        Assert.assertTrue(TeamManager.getInstance().isUserBelongToTeam("SW","demo-user"));
    }
    // --- END update user team to non exist team ---

    //==========================================================================================
    //                            End Update User Team Testing
    //==========================================================================================

    //==========================================================================================
    //                                   Login Testing
    //==========================================================================================

    // --- CHECK login user not exist ---
    @Test
    public void testLoginWhitNotExistUser() {
        System.out.println("User Manager:: login user not exist [ expected result: fail ]");

        userManager.login("demo1","123");

        Assert.assertNotSame(UserManager.getInstance().loggedInUser,userManager.users.get("demo1"));
    }
    // --- END login user not exist ---

    // --- CHECK login with wrong password ---
    @Test
    public void testLoginWhitWrongPass() {
        System.out.println("User Manager:: login with wrong password [ expected result: fail ]");

        userManager.login("demo-user","123");

        Assert.assertNotSame(UserManager.getInstance().loggedInUser,userManager.users.get("demo-test"));
    }
    // --- END  login with wrong password  ---

    // --- CHECK valid login ---
    @Test
    public void testValidLogin() {
        System.out.println("User Manager:: login [ expected result: success ]");

        userManager.login("demo-user","test");

        Assert.assertSame(UserManager.getInstance().loggedInUser,userManager.users.get("demo-user"));
    }
    // --- END valid login ---

    //==========================================================================================
    //                                  END Login Testing
    //==========================================================================================



   /* @AfterClass
    public static void deleteTestFile() {
        //String testPath = "src/com/data/teamsFileTest.ser";
        //File testFile = new File(userTestPath);
        if (testFile.exists())
            testFile.delete();
    }*/
}
