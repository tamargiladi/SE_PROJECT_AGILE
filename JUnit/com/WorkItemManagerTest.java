import com.business.TeamManager;
import com.business.WorkItemManager;
import com.persistent.WorkItem;
import org.junit.*;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;


public class WorkItemManagerTest {

    WorkItemManager workItemManager;

    @Before
    public void setUpMethod() {
        System.out.println("Set Up Method");
        workItemManager = WorkItemManager.getInstance();
    }

    @After
    public void tearDownMethod() {
        System.out.println("Tear Down Method");
        workItemManager = null;
    }

    // ---- Test new work items creation ----

    // --> Test creation of epics

    @Test
    public void testCreateInvalidEpicMissingFields() {
        System.out.println("Work Item Manager::Create new Work Item::Epic - invalid work items creation (missing field: summary && description) [expected result: fail]");
        Assert.assertNull("Creating invalid epic (missing summary & description)",workItemManager.createNewWorkItem()
                .build(WorkItem.typeEnum.Epic, null));
    }

    @Test
    public void testCreateInvalidEpicMissingFieldDesc() {
        System.out.println("Work Item Manager::Create new Work Item::Epic - invalid work items creation (missing field: description) [expected result: fail]");
        Assert.assertNull("Creating invalid epic (missing description)", workItemManager.createNewWorkItem()
                .withSummary("with summary test")
                .build(WorkItem.typeEnum.Epic, null));
    }

    @Test
    public void testCreateInvalidEpicMissingFieldSummary() {
        System.out.println("Work Item Manager::Create new Work Item::Epic - invalid work items creation (missing field: summary) [expected result: fail]");
        Assert.assertNull("Creating invalid epic (missing description)", workItemManager.createNewWorkItem()
                .withDescription("with description test")
                .build(WorkItem.typeEnum.Epic, null));
    }

    @Test
    public void testCreateValidEpic() {
        System.out.println("Work Item Manager::Create new Work Item::Epic - valid work items creation [expected result: success]");
        Assert.assertNotNull("Creating valid epic", workItemManager.createNewWorkItem()
                .withDescription("with description test")
                .withSummary("with summary test")
                .build(WorkItem.typeEnum.Epic, null));
    }

    // --> Test creation of stories

    @Test
    public void testCreateInvalidStoryMissingFields() {
        System.out.println("Work Item Manager::Create new Work Item::Story - invalid work items creation (missing field: summary && description) [expected result: fail]");
        Assert.assertNull(workItemManager.createNewWorkItem()
                .build(WorkItem.typeEnum.Story, null));
    }

    @Test
    public void testCreateInvalidStoryMissingFieldDesc() {
        System.out.println("Work Item Manager::Create new Work Item::Story - invalid work items creation (missing field: description) [expected result: fail]");
        Assert.assertNull(workItemManager.createNewWorkItem()
                .withSummary("with summary test")
                .build(WorkItem.typeEnum.Story, null));
    }

    @Test
    public void testCreateInvalidStoryMissingFieldSummary() {
        System.out.println("Work Item Manager::Create new Work Item::Story - invalid work items creation (missing field: summary) [expected result: fail]");
        Assert.assertNull(workItemManager.createNewWorkItem()
                .withDescription("with description test")
                .build(WorkItem.typeEnum.Story, null));
    }

    @Test
    public void testCreateValidStory() {
        System.out.println("Work Item Manager::Create new Work Item::Story - valid work items creation [expected result: success]");
        Assert.assertNotNull(workItemManager.createNewWorkItem()
                .withDescription("with description test")
                .withSummary("with summary test")
                .build(WorkItem.typeEnum.Story, null));
    }

    // --> Test creation of Tasks

    @Test
    public void testCreateInvalidTaskMissingFields() {
        System.out.println("Work Item Manager::Create new Work Item::Task - invalid work items creation (missing field: summary && description) [expected result: fail]");
        Assert.assertNull(workItemManager.createNewWorkItem()
                .build(WorkItem.typeEnum.Task, null));
    }

    @Test
    public void testCreateInvalidTaskMissingFieldDesc() {
        System.out.println("Work Item Manager::Create new Work Item::Task - invalid work items creation (missing field: description) [expected result: fail]");
        Assert.assertNull(workItemManager.createNewWorkItem()
                .withSummary("with summary test")
                .build(WorkItem.typeEnum.Task, null));
    }

    @Test
    public void testCreateInvalidTaskMissingFieldSummary() {
        System.out.println("Work Item Manager::Create new Work Item::Task - invalid work items creation (missing field: summary) [expected result: fail]");
        Assert.assertNull(workItemManager.createNewWorkItem()
                .withDescription("with description test")
                .build(WorkItem.typeEnum.Task, null));
    }

    @Test
    public void testCreateValidTask() {
        System.out.println("Work Item Manager::Create new Work Item::Task - valid work items creation [expected result: success]");
        Assert.assertNotNull(workItemManager.createNewWorkItem()
                .withDescription("with description test")
                .withSummary("with summary test")
                .build(WorkItem.typeEnum.Task, null));
    }

    // --> Test creation of Bugs

    @Test
    public void testCreateInvalidBugMissingFields() {
        System.out.println("Work Item Manager::Create new Work Item::Bug - invalid work items creation (missing field: summary && description) [expected result: fail]");
        Assert.assertNull(workItemManager.createNewWorkItem()
                .build(WorkItem.typeEnum.Bug, null));
    }

    @Test
    public void testCreateInvalidBugMissingFieldDesc() {
        System.out.println("Work Item Manager::Create new Work Item::Bug - invalid work items creation (missing field: description) [expected result: fail]");
        Assert.assertNull (workItemManager.createNewWorkItem()
                .withSummary("with summary test")
                .build(WorkItem.typeEnum.Bug, null));
    }

    @Test
    public void testCreateInvalidBugMissingFieldSummary() {
        System.out.println("Work Item Manager::Create new Work Item::Bug - invalid work items creation (missing field: summary) [expected result: fail]");
        Assert.assertNull(workItemManager.createNewWorkItem()
                .withDescription("with description test")
                .build(WorkItem.typeEnum.Bug, null));
    }

    @Test
    public void testCreateValidBug() {
        System.out.println("Work Item Manager::Create new Work Item::Bug - valid work items creation [expected result: success]");
        Assert.assertNotNull(workItemManager.createNewWorkItem()
                .withDescription("with description test")
                .withSummary("with summary test")
                .build(WorkItem.typeEnum.Bug, null));
    }

    // ---- Test work item ID auto increase ----

    @Test
    public void testNextAvailableIdValid() {
        System.out.println("Work Item Manager::Get Next Available ID - valid work items creation [expected result: success]");
        Integer initialID = workItemManager.getNextAvailableId();
        //create new work items successfully
        workItemManager.createNewWorkItem().withDescription("with description test").withSummary("with summary test")
                        .build(WorkItem.typeEnum.Bug, null);
        Integer currentID = workItemManager.getNextAvailableId();
        Assert.assertEquals(java.util.Optional.ofNullable(currentID), java.util.Optional.ofNullable(initialID + 1));
    }

    @Test
    public void testNextAvailableIdInvalid() {
        System.out.println("Work Item Manager::Get Next Available ID - invalid work items creation [expected result: fail]");
        Integer initialID = workItemManager.getNextAvailableId();
        //create new work items unsuccessfully
        workItemManager.createNewWorkItem().build(WorkItem.typeEnum.Bug, null);
        Integer currentID = workItemManager.getNextAvailableId();
        Assert.assertNotEquals(java.util.Optional.ofNullable(currentID), java.util.Optional.ofNullable(initialID + 1));
    }

    // ---- Test work item addition to hashmap ----

    @Test
    public void testAddValidWorkItemToHashMap() {
        System.out.println("Work Item Manager::Add Work Item To HashMap - valid work items creation [expected result: success]");
        Assert.assertFalse(workItemManager.workItems.containsKey(WorkItemManager.getAvailableId()));
        WorkItem newWorkItem = workItemManager.createNewWorkItem().withDescription("description test").withSummary("summary test")
                .build(WorkItem.typeEnum.Bug, null);
        workItemManager.addWorkItemToHashMap(newWorkItem, true);
        Assert.assertTrue(workItemManager.workItems.containsKey(newWorkItem.getId()));
        Assert.assertTrue(workItemManager.workItems.containsValue(newWorkItem));

    }

    @Test
    public void testAddInvalidWorkItemToHashMap() {
        System.out.println("Work Item Manager::Add Work Item To HashMap - invalid work items creation [expected result: fail]");
        Assert.assertFalse(workItemManager.workItems.containsKey(WorkItemManager.getAvailableId()));
        WorkItem newWorkItem = workItemManager.createNewWorkItem().build(WorkItem.typeEnum.Bug, null);
        workItemManager.addWorkItemToHashMap(newWorkItem, true);
        Assert.assertFalse(workItemManager.workItems.containsValue(newWorkItem));
    }

    // ---- Test search work item ----

    @Test
    public void testSearchValidWorkItem() {
        System.out.println("Work Item Manager::Search Work Item - valid work item [expected result: success]");
        WorkItem newWorkItem = workItemManager.createNewWorkItem().withDescription("description test").withSummary("summary test")
                .build(WorkItem.typeEnum.Bug, null);
        Assert.assertNotNull(workItemManager.searchWorkItem(newWorkItem.getId()));
    }

    @Test
    public void testSearchInvalidWorkItem() {
        System.out.println("Work Item Manager::Search Work Item - invalid work item [expected result: fail]");
        Integer futureWorkItemID =  workItemManager.getNextAvailableId();
        Assert.assertNull(workItemManager.searchWorkItem(futureWorkItemID));
    }

    @Test
    public void testSearchWorkItemNegativeValue() {
        System.out.println("Work Item Manager::Search Work Item - negative work item ID [expected result: fail]");
        int rand = new Random().nextInt(99) - 100; //generate random number in range [-100, -1]
        Assert.assertNull(workItemManager.searchWorkItem(rand));
    }

    // ---- Test save & load: work item hashmap <-> work items file ----

    @Test
    public void testCreateWorkItemsFile() throws IOException {
        System.out.println("Work Item Manager:: Create new work items file [expected result: success]");
        String testPath = "src/com/data/workItemsTest.xml";
        workItemManager.setFileAddress(testPath);
        File testFile = new File(testPath);
        if (testFile.exists())
            testFile.delete();
        workItemManager.loadWorkItemFileToHashMap();
        Assert.assertFalse(testFile.createNewFile());
    }

    @Test
    public void testSaveWorkItemsToFile() throws IOException {
        System.out.println("Work Item Manager:: Save work item to work items file [expected result: success]");
        String testPath = "src/com/data/workItemsTest.xml";
        workItemManager.setFileAddress(testPath);
        WorkItem newWorkItem = workItemManager.createNewWorkItem().withDescription("new bug").withSummary("new bug")
                .build(WorkItem.typeEnum.Bug, null);
        Assert.assertTrue(workItemManager.updateWorkItemsFile());
    }

    @Test
    public void testLoadWorkItemsToFile() throws IOException {
        System.out.println("Work Item Manager:: Load work items to work items file [expected result: success]");
        String testPath = "src/com/data/workItemsTest.xml";
        workItemManager.setFileAddress(testPath);
        WorkItem newWorkItem = workItemManager.createNewWorkItem().withDescription("new task").withSummary("new task")
                .build(WorkItem.typeEnum.Task, null);
        workItemManager.updateWorkItemsFile();
        workItemManager.workItems.clear();
        workItemManager.loadWorkItemFileToHashMap();
        Assert.assertTrue(workItemManager.workItems.containsKey(newWorkItem.getId()));
    }

    // ---- Test save work item fields ----

    @Test
    public void testSaveWorkItemSummary() {
        System.out.println("Work Item Manager:: Save work item field::summary [expected result: success]");
        WorkItem workItem = workItemManager.createNewWorkItem().withSummary("summary test case").withDescription("Test")
                .build(WorkItem.typeEnum.Epic, null);
        Assert.assertEquals(workItem.getSummary(), "summary test case");
    }

    @Test
    public void testSaveWorkItemStatus() {
        System.out.println("Work Item Manager:: Save work item field::status [expected result: success]");
        WorkItem workItem = workItemManager.createNewWorkItem().withSummary("status test case").withDescription("Test")
                .withStatus(WorkItem.statusEnum.Done)
                .build(WorkItem.typeEnum.Epic, null);
        Assert.assertEquals(workItem.getStatus(), WorkItem.statusEnum.Done);
    }

    @Test
    public void testSaveWorkItemDescription() {
        System.out.println("Work Item Manager:: Save work item field::description [expected result: success]");
        WorkItem workItem = workItemManager.createNewWorkItem().withSummary("description test case").withDescription("Test - description")
                .build(WorkItem.typeEnum.Epic,null);
        Assert.assertEquals(workItem.getDescription(), "Test - description");
    }

    @Test
    public void testSaveWorkItemPriority() {
        System.out.println("Work Item Manager:: Save work item field::priority [expected result: success]");
        WorkItem workItem = workItemManager.createNewWorkItem().withSummary("priority test case").withDescription("Test")
                .withPriority(WorkItem.priorityEnum.Low)
                .build(WorkItem.typeEnum.Story,null);
        Assert.assertEquals(workItem.getPriority(), WorkItem.priorityEnum.Low);
    }

    @Test
    public void testSaveWorkItemOwner() {
        System.out.println("Work Item Manager:: Save work item field::owner [expected result: success]");
        WorkItem workItem = workItemManager.createNewWorkItem().withSummary("owner test case").withDescription("Test")
                .withOwner("admin")
                .build(WorkItem.typeEnum.Story,null);
        Assert.assertEquals(workItem.getOwner(), "admin");
    }

    @Test
    public void testSaveWorkItemEpicId() {
        System.out.println("Work Item Manager:: Save work item field::epic id [expected result: success]");
        int rand = new Random().nextInt(1000);
        WorkItem workItem = workItemManager.createNewWorkItem().withSummary("epic id test case").withDescription("Test")
                .withEpicID(rand)
                .build(WorkItem.typeEnum.Story,null);
        Assert.assertEquals(java.util.Optional.ofNullable(workItem.getEpicID()), java.util.Optional.ofNullable(rand));
    }

    @Test
    public void testSaveWorkItemTeam() {
        System.out.println("Work Item Manager:: Save work item field::team [expected result: success]");
        WorkItem workItem = workItemManager.createNewWorkItem().withSummary("team test case").withDescription("Test")
                .withTeam("teamName")
                .build(WorkItem.typeEnum.Task,null);
        Assert.assertEquals(workItem.getTeam(), "teamName");
    }

    @Test
    public void testSaveWorkItemSprint() {
        System.out.println("Work Item Manager:: Save work item field::sprint [expected result: success]");
        WorkItem workItem = workItemManager.createNewWorkItem().withSummary("sprint test case").withDescription("Test")
                .withSprint(WorkItem.sprintEnum.Apr20)
                .build(WorkItem.typeEnum.Task,null);
        Assert.assertEquals(workItem.getSprint(), WorkItem.sprintEnum.Apr20);
    }

    @Test
    public void testSaveWorkItemEstimate() {
        System.out.println("Work Item Manager:: Save work item field::estimate [expected result: success]");
        int rand = new Random().nextInt(1000);
        WorkItem workItem = workItemManager.createNewWorkItem().withSummary("estimate test case").withDescription("Test")
                .withEstimate(rand)
                .build(WorkItem.typeEnum.Task, null);
        Assert.assertEquals(java.util.Optional.ofNullable(workItem.getEstimate()), java.util.Optional.ofNullable(rand));
    }

    @Test
    public void testSaveWorkItemTimeSpent() {
        System.out.println("Work Item Manager:: Save work item field::time spent [expected result: success]");
        int rand = new Random().nextInt(1000);
        WorkItem workItem = workItemManager.createNewWorkItem().withSummary("time spent test case").withDescription("Test")
                .withTimeSpent(rand)
                .build(WorkItem.typeEnum.Task, null);
        Assert.assertEquals(java.util.Optional.ofNullable(workItem.getTimeSpent()), java.util.Optional.ofNullable(rand));
    }

    @Test
    public void testSaveWorkItemTargetVersion() {
        System.out.println("Work Item Manager:: Save work item field::target version [expected result: success]");
        WorkItem workItem = workItemManager.createNewWorkItem().withSummary("target version test case").withDescription("Test")
                .withTargetVersion("target-ver")
                .build(WorkItem.typeEnum.Task, null);
        Assert.assertEquals(workItem.getTargetVersion(), "target-ver");
    }

    @Test
    public void testSaveWorkItemStoryId() {
        System.out.println("Work Item Manager:: Save work item field::story id [expected result: success]");
        int rand = new Random().nextInt(1000);
        WorkItem workItem = workItemManager.createNewWorkItem().withSummary("story id test case").withDescription("Test")
                .withStoryID(rand)
                .build(WorkItem.typeEnum.Task, null);
        Assert.assertEquals(java.util.Optional.ofNullable(workItem.getStoryID()), java.util.Optional.ofNullable(rand));
    }

    @Test
    public void testSaveWorkItemFoundVersion() {
        System.out.println("Work Item Manager:: Save work item field::found version [expected result: success]");
        WorkItem workItem = workItemManager.createNewWorkItem().withSummary("found version test case").withDescription("Test")
                .withTargetVersion("found-ver")
                .build(WorkItem.typeEnum.Bug, null);
        Assert.assertEquals(workItem.getTargetVersion(), "found-ver");
    }


    // --- After all tests ---

    @AfterClass
    public static void deleteTestFile() {
        String testPath = "src/com/data/workItemsTest.xml";
        File testFile = new File(testPath);
        if (testFile.exists())
            testFile.delete();
    }



}