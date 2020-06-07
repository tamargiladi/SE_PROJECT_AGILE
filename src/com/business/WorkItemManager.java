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

    private static String fileAddress = "src/com/data/workItems.xml";
    public HashMap<Integer, WorkItem> workItems;
    private static Integer nextAvailableId = 100;

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

    public static Integer getAvailableId() {
        return nextAvailableId;
    }

    public static void increaseNextAvailableId() {
        System.out.println("Increasing Work Item next available ID");
        nextAvailableId++;
    }


    // Addition of new work item to hashmap
    public void addWorkItemToHashMap(WorkItem wi, boolean isNew) {
        workItems.put(wi.getId(), wi);
        if (isNew) {
            System.out.println("Saved NEW work item: " + wi.getType() + " " +  wi.getId());
            increaseNextAvailableId();
        }
        else
            System.out.println("Saved EXISTED work item: " + wi.getType() + " " + wi.getId());
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
    public void loadWorkItemFileToHashMap() {
        try {
            FileInputStream workItemsFile = new FileInputStream(fileAddress);
            XMLDecoder decoder = new XMLDecoder(workItemsFile);
            WorkItem wi = (WorkItem) decoder.readObject();
            while (wi != null) {
                addWorkItemToHashMap(wi, true);
                wi = (WorkItem) decoder.readObject();
            }
            decoder.close();
            workItemsFile.close();
            System.out.println("Loading work items from database to hashmap: succeeded");
        }
        catch (Exception e){
            System.out.println("Loading work items from database to hashmap: failed");
            e.printStackTrace();
        }
    }

    // Overwrites work items file with the hashmap contents
    public void updateWorkItemsFile() {
        FileOutputStream workItemsFile = null;
        try {
            workItemsFile = new FileOutputStream(fileAddress);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (workItemsFile != null) {
            try {
                XMLEncoder encoder = new XMLEncoder(workItemsFile);
                for (Map.Entry<Integer, WorkItem> entry : workItems.entrySet()) {
                    encoder.writeObject(entry.getValue());
                }
                encoder.close();
                workItemsFile.close();
                System.out.println("Loading work items from hashmap to database: succeeded");
            } catch (IOException e) {
                System.out.println("Loading work items from hashmap to database: failed");
                e.printStackTrace();
            }
        }
    }


}


