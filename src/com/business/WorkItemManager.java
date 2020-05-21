package com.business;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;
import com.persistent.*;


public class WorkItemManager {
    

    private static String fileAddress = "src/com/data/workItems.xml";
    public HashMap<Integer, WorkItem> workItems;
    private static Integer nextAvailableId = 100;


    public WorkItemManager() {
        try {
            workItems = new HashMap<>();
            loadWorkItemFileToHashMap();
            nextAvailableId = Collections.max(workItems.keySet()) + 1;
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
        nextAvailableId++;
    }


    // Create new work item - not saved to hashmap or file
    public WorkItem createNewWI(WorkItem.typeEnum type) {
        WorkItem wi;
        if (type == WorkItem.typeEnum.Epic) { wi = new EpicWI(); }
        else if (type == WorkItem.typeEnum.Story) { wi = new StoryWI(); }
        else if (type == WorkItem.typeEnum.Task) { wi = new TaskWI(); }
        else { wi = new BugWI(); } //bug
        return wi;
    }

    // Addition of new work item to hashmap
    public void addWorkItemToHashMap(WorkItem wi, boolean isNew) {
        workItems.put(wi.getId(), wi);
        if (isNew)
            increaseNextAvailableId();
    }

    // Save Work Item
    public void saveWorkItem(WorkItem wi, String summary, WorkItem.statusEnum status, String description, WorkItem.priorityEnum priority, String owner,
                             Integer epicID, String team, WorkItem.sprintEnum sprint, Integer estimate, Integer timeSpent, String targetVersion,
                             Integer storyID, String foundVersion, boolean isNew)
    {
        wi.updateWorkItem(summary, status, description, priority, owner, epicID, team, sprint, estimate, timeSpent, targetVersion, storyID, foundVersion);
        addWorkItemToHashMap(wi, isNew);
    }


    // search work item by id
    public WorkItem searchWorkItem(Integer id) {
        if (workItems.containsKey(id))
            return workItems.get(id);
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
        }
        catch (Exception e){
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}


