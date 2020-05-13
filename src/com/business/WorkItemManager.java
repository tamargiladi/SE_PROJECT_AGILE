package com.business;

public class WorkItemManager {

    private static Integer nextAvailableId;

    public WorkItemManager() {

    }

    public static Integer getAvailableId() {
        return nextAvailableId;
    }

}
