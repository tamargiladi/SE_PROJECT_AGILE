package com.business;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;
import com.persistent.*;

/***
Singleton class:
 Managing work items documentation, responsible for creating and editing work items
 ***/
public class WorkItemManager {

    private static WorkItemManager WorkItemManagerInstance; //Singleton instance
    private static String workItemFileAddress = "src/com/data/workItems.xml";
    public HashMap<Integer, WorkItem> workItems;
    private static Integer nextAvailableId = 100;
    private WorkItem.sprintEnum currentSprint;

    public static WorkItemManager getInstance() {
        if (WorkItemManagerInstance == null)
            WorkItemManagerInstance = new WorkItemManager();
        return  WorkItemManagerInstance;
    }

    private WorkItemManager() {
        try {
            workItems = new HashMap<>();
            loadWorkItemFileToHashMap(); //creates work items file if not exists, read from file to hashmap if exists
            nextAvailableId = Collections.max(workItems.keySet()) + 1; //init work item id counter
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("File must be empty. Continue...");
        }
    }

    public WorkItemBuilder createNewWorkItem() {
        return WorkItemBuilder.builder();
    }

    public static void increaseNextAvailableId() {
        nextAvailableId++;
    }

    // Addition of new work item to hashmap
    public boolean addWorkItemToHashMap(WorkItem wi, boolean isNew) {
        if (wi != null) {
            workItems.put(wi.getId(), wi);
            if (isNew)
                increaseNextAvailableId();
            return true;
        }
        else
            return false;
    }

    // search work item by id
    public WorkItem searchWorkItem(Integer id) {
        if (workItems.containsKey(id)) {
            System.out.println("Work Item " + id + ": found a match");
            return workItems.get(id);
        }
        System.out.println("Work Item " + id + ": could not found a match");
        return null;
    }

    // Loading work items file contents into hashmap
    public boolean loadWorkItemFileToHashMap() {
        try {
            File wiFile = new File(workItemFileAddress);
            if (!(wiFile.createNewFile())) {
                FileInputStream workItemsFile = new FileInputStream(workItemFileAddress);
                XMLDecoder decoder = new XMLDecoder(workItemsFile);
                workItems = (HashMap<Integer, WorkItem>) decoder.readObject();
                currentSprint = (WorkItem.sprintEnum) decoder.readObject();
                decoder.close();
                workItemsFile.close();
                return true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    // Overwrites work items file with the hashmap contents
    public boolean updateWorkItemsFile() {
        FileOutputStream workItemsFile = null;
        try {
            workItemsFile = new FileOutputStream(workItemFileAddress);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        if (workItemsFile != null) {
            try {
                XMLEncoder encoder = new XMLEncoder(workItemsFile);
                encoder.writeObject(workItems);
                encoder.writeObject(currentSprint);

                encoder.close();
                workItemsFile.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }


    // getters/setters
    public static Integer getAvailableId() {
        return nextAvailableId;
    }

    public WorkItem.sprintEnum getCurrentSprint() {
        return currentSprint;
    }

    public void setCurrentSprint(WorkItem.sprintEnum currentSprint) {
        this.currentSprint = currentSprint;
    }

    public static Integer getNextAvailableId() {
        return nextAvailableId;
    }

    // only change for test purposes
    public static void setWorkItemFileAddress(String workItemFileAddress) {
        WorkItemManager.workItemFileAddress = workItemFileAddress;
    }

}


