import com.business.ReportGenerator;
import com.business.UserManager;
import com.business.WorkItemManager;
import com.persistent.User;
import com.persistent.WorkItem;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/***
 Testing of ReportGenerator class
 ***/

public class ReportGeneratorTest {

    WorkItemManager workItemManager;
    ReportGenerator reportGenerator;
    UserManager userManager;

    @Before
    public void setUpMethod() {
        System.out.println("Set Up Method");
        workItemManager = WorkItemManager.getInstance();
        reportGenerator = new ReportGenerator();
        userManager = UserManager.getInstance();

        userManager.login("admin", "admin");
        userManager.addUser("demo-user","test", User.PermissionLevel.admin,"default");
    }

    @After
    public void tearDownMethod() {
        System.out.println("Tear Down Method");
        userManager.login("admin", "admin");
        userManager.removeUser("demo-user");
        workItemManager = null;
        reportGenerator = null;
        userManager = null;
    }

    // --- check permission levels ---

    @Test
    public void testActionPermissionAdmin() {
        System.out.println("Report Generator::permission test - admin [expected result: permitted]");
        userManager.login("demo-user", "test");
        Assert.assertTrue("Permission test as admin", reportGenerator.isActionPermitted());
    }

    @Test
    public void testActionPermissionManager() {
        System.out.println("Report Generator::permission test - manager [expected result: permitted]");
        userManager.updateUserPermission("demo-user", User.PermissionLevel.manager);
        userManager.login("demo-user", "test");
        Assert.assertTrue("Permission test as manager", reportGenerator.isActionPermitted());
    }

    @Test
    public void testActionPermissionMember() {
        System.out.println("Report Generator::permission test - member [expected result: not permitted]");
        userManager.updateUserPermission("demo-user", User.PermissionLevel.member);
        userManager.login("demo-user", "test");
        Assert.assertFalse("Permission test as member", reportGenerator.isActionPermitted());
    }

    // -- Test Reports output --
    @Test
    public void testTotalPlannedHoursPerMember() {
        System.out.println("Report Generator::Report: Total Planned Hours Per Member");
        reportGenerator.setChosenSprint(WorkItem.sprintEnum.Jul20);
        int rand1 = new Random().nextInt(100);
        int rand2 = new Random().nextInt(100);
        int rand3 = new Random().nextInt(100);

        workItemManager.createNewWorkItem().withDescription("demo task (1)").withSummary("demo task 1")
                .withOwner("demo-user")
                .withEstimate(rand1)
                .withSprint(WorkItem.sprintEnum.Jul20)
                .build(WorkItem.typeEnum.Task,null);
        workItemManager.createNewWorkItem().withDescription("demo bug (2)").withSummary("demo bug 2")
                .withOwner("demo-user")
                .withEstimate(rand2)
                .withSprint(WorkItem.sprintEnum.Jul20)
                .build(WorkItem.typeEnum.Bug,null);
        workItemManager.createNewWorkItem().withDescription("demo task (3)").withSummary("demo task 3")
                .withOwner("demo-user")
                .withEstimate(rand3)
                .withSprint(WorkItem.sprintEnum.Jul20)
                .build(WorkItem.typeEnum.Task,null);

        Assert.assertEquals(java.util.Optional.of(reportGenerator.totalPlannedHoursPerMember().get("demo-user")), java.util.Optional.of((rand1 + rand2 + rand3)));
    }

    @Test
    public void testWorkItemStatusDistribution() {
        System.out.println("Report Generator::Report: Total Work Items per Status distribution");
        reportGenerator.setChosenSprint(WorkItem.sprintEnum.Jul20);
        workItemManager.workItems.clear();

        workItemManager.createNewWorkItem().withDescription("demo task (1) - new").withSummary("demo task 1")
                .withSprint(WorkItem.sprintEnum.Jul20)
                .withStatus(WorkItem.statusEnum.New)
                .build(WorkItem.typeEnum.Task,null);
        workItemManager.createNewWorkItem().withDescription("demo bug (2) - in progress").withSummary("demo bug 2")
                .withSprint(WorkItem.sprintEnum.Jul20)
                .withStatus(WorkItem.statusEnum.InProgress)
                .build(WorkItem.typeEnum.Bug,null);
        workItemManager.createNewWorkItem().withDescription("demo task (3) - done").withSummary("demo task 3")
                .withSprint(WorkItem.sprintEnum.Jul20)
                .withStatus(WorkItem.statusEnum.Done)
                .build(WorkItem.typeEnum.Task,null);
        workItemManager.createNewWorkItem().withDescription("demo bug (4) - new").withSummary("demo bug 4")
                .withSprint(WorkItem.sprintEnum.Jul20)
                .withStatus(WorkItem.statusEnum.New)
                .build(WorkItem.typeEnum.Bug,null);
        //add work item to another sprint - make sure it's not in counted in the report output:
        workItemManager.createNewWorkItem().withDescription("demo bug (5) - done - NOT IN SPRINT").withSummary("demo bug 5")
                .withSprint(WorkItem.sprintEnum.Aug20)
                .withStatus(WorkItem.statusEnum.New)
                .build(WorkItem.typeEnum.Bug,null);

        Assert.assertEquals(java.util.Optional.ofNullable(reportGenerator.workItemStatusDistribution().get(WorkItem.statusEnum.New)), java.util.Optional.ofNullable(2));
        Assert.assertEquals(java.util.Optional.ofNullable(reportGenerator.workItemStatusDistribution().get(WorkItem.statusEnum.InProgress)), java.util.Optional.ofNullable(1));
        Assert.assertEquals(java.util.Optional.ofNullable(reportGenerator.workItemStatusDistribution().get(WorkItem.statusEnum.Done)), java.util.Optional.ofNullable(1));
    }

    @Test
    public void testBugsFoundInVersion() {
        System.out.println("Report Generator::Report: Bugs found in version");
        reportGenerator.setChosenVersion("1.0.0-demo");
        workItemManager.workItems.clear();

        // bugs to be counted - found on chosen version
        WorkItem bug1 = workItemManager.createNewWorkItem().withDescription("demo bug (1) - valid: found in chosen version").withSummary("demo bug 1")
                .withFoundVersion("1.0.0-demo")
                .build(WorkItem.typeEnum.Bug,null);
        WorkItem bug2 = workItemManager.createNewWorkItem().withDescription("demo bug (2) - valid: found in chosen version").withSummary("demo bug 2")
                .withFoundVersion("1.0.0-demo")
                .build(WorkItem.typeEnum.Bug,null);
        // task - type should not be counted
        WorkItem task3 = workItemManager.createNewWorkItem().withDescription("demo task (3) - invalid: task type should be in report output").withSummary("demo task 3")
                .withFoundVersion("1.0.0-demo")
                .build(WorkItem.typeEnum.Task,null);
        // bug found in different version - should not be counted
        WorkItem bug4 = workItemManager.createNewWorkItem().withDescription("demo bug (4) - invalid: bug found in different version").withSummary("demo bug 4")
                .withFoundVersion("2.0.0-demo")
                .build(WorkItem.typeEnum.Bug,null);

        // assert - there are 2 bugs found in the report
        Assert.assertEquals(java.util.Optional.ofNullable(reportGenerator.bugsFoundInVersion().size()), java.util.Optional.ofNullable(2));
        // assert - the 2 bugs found are the correct ones
        Assert.assertTrue(reportGenerator.bugsFoundInVersion().contains(bug1));
        Assert.assertTrue(reportGenerator.bugsFoundInVersion().contains(bug2));

    }

    @Test
    public void testBugsSolvedInVersion() {
        System.out.println("Report Generator::Report: Bugs solved in version");
        reportGenerator.setChosenVersion("1.0.0-demo");
        workItemManager.workItems.clear();

        // bugs to be counted - targeted on chosen version & status=Done
        WorkItem bug1 = workItemManager.createNewWorkItem().withDescription("demo bug (1) - solved on chosen version").withSummary("demo bug 1")
                .withTargetVersion("1.0.0-demo")
                .withStatus(WorkItem.statusEnum.Done)
                .build(WorkItem.typeEnum.Bug,null);
        WorkItem bug2 = workItemManager.createNewWorkItem().withDescription("demo bug (2) - solved on chosen version").withSummary("demo bug 2")
                .withTargetVersion("1.0.0-demo")
                .withStatus(WorkItem.statusEnum.Done)
                .build(WorkItem.typeEnum.Bug,null);
        // task - type should not be counted
        WorkItem task3 = workItemManager.createNewWorkItem().withDescription("demo task (3) - report should not output tasks").withSummary("demo task 3")
                .withFoundVersion("1.0.0-demo")
                .build(WorkItem.typeEnum.Task,null);
        // bug solved in different version - should not be counted
        WorkItem bug4 = workItemManager.createNewWorkItem().withDescription("demo bug (4) - solved on different version").withSummary("demo bug 4")
                .withTargetVersion("2.0.0-demo")
                .withStatus(WorkItem.statusEnum.Done)
                .build(WorkItem.typeEnum.Bug,null);
        // bug targeted to chosen version but status is In Progress - should not be counted
        WorkItem bug5 = workItemManager.createNewWorkItem().withDescription("demo bug (5) - target version is correct but status is not done").withSummary("demo bug 5")
                .withTargetVersion("1.0.0-demo")
                .withStatus(WorkItem.statusEnum.InProgress)
                .build(WorkItem.typeEnum.Bug,null);

        // assert - there are 2 bugs found in the report
        Assert.assertEquals(java.util.Optional.ofNullable(reportGenerator.bugsSolvedInVersion().size()), java.util.Optional.ofNullable(2));
        // assert - the 2 bugs found are the correct ones
        Assert.assertTrue(reportGenerator.bugsSolvedInVersion().contains(bug1));
        Assert.assertTrue(reportGenerator.bugsSolvedInVersion().contains(bug2));

    }

    @Test
    public void testExceedingEstimations() {
        System.out.println("Report Generator::Report: Bugs solved in version");
        workItemManager.workItems.clear();
        int rand1 = new Random().nextInt(100);
        int rand2 = new Random().nextInt(100);

        // bugs & tasks to be counted - time spent > estimation
        WorkItem bug1 = workItemManager.createNewWorkItem().withDescription("demo bug (1) - valid: bug: time spent > estimation").withSummary("demo bug 1")
                .withTimeSpent(rand1 * 2)
                .withEstimate(rand1)
                .build(WorkItem.typeEnum.Bug,null);
        WorkItem task2 = workItemManager.createNewWorkItem().withDescription("demo task (2) - valid: task: time spent > estimation").withSummary("demo task 2")
                .withTimeSpent(rand2 + 1)
                .withEstimate(rand2)
                .build(WorkItem.typeEnum.Task,null);
        // task: time spent = estimation: should not be counted
        WorkItem task3 = workItemManager.createNewWorkItem().withDescription("demo task (3) - invalid: task: time spent = estimation").withSummary("demo task 3")
                .withTimeSpent(rand1)
                .withEstimate(rand1)
                .build(WorkItem.typeEnum.Task,null);
        // bug: time spent < estimation  - should not be counted
        WorkItem bug4 = workItemManager.createNewWorkItem().withDescription("demo bug (4) - invalid: bug: time spent < estimation").withSummary("demo bug 4")
                .withTimeSpent(rand2)
                .withEstimate(rand2 * 2)
                .build(WorkItem.typeEnum.Bug,null);
        // story: time spent > estimation but still should not be counted - reports addresses bugs & tasks only
        WorkItem story5 = workItemManager.createNewWorkItem().withDescription("demo story (5) - invalid: story: time spent > estimation (stories not included in report)").withSummary("demo bug 5")
                .withTimeSpent(rand2 * 2)
                .withEstimate(rand2)
                .build(WorkItem.typeEnum.Story,null);
        // epic: time spent > estimation but still should not be counted - reports addresses bugs & tasks only
        WorkItem epic6 = workItemManager.createNewWorkItem().withDescription("demo story (6) - invalid: epic: time spent > estimation (epics not included in report)").withSummary("demo epic 6")
                .withTimeSpent(rand1 * 2)
                .withEstimate(rand1)
                .build(WorkItem.typeEnum.Epic,null);

        // assert - there are 2 bugs found in the report
        Assert.assertEquals(java.util.Optional.ofNullable(reportGenerator.exceedingEstimations().size()), java.util.Optional.ofNullable(2));
        // assert - the 2 bugs found are the correct ones
        Assert.assertTrue(reportGenerator.exceedingEstimations().contains(bug1));
        Assert.assertTrue(reportGenerator.exceedingEstimations().contains(task2));
    }

}
