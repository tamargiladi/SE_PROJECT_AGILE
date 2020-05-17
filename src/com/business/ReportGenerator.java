package com.business;

import com.persistent.User;
import com.persistent.WorkItem;
import com.business.WorkItemManager.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportGenerator {

    private String chosenVersion;
    private WorkItem.sprintEnum chosenSprint;
    private static HashMap<Integer, WorkItem> hashMap;

    public ReportGenerator(HashMap<Integer, WorkItem> workItems) {
        hashMap = workItems;
    }

    public String getChosenVersion() {
        return chosenVersion;
    }

    public void setChosenVersion(String chosenVersion) {
        this.chosenVersion = chosenVersion;
    }

    public WorkItem.sprintEnum getChosenSprint() {
        return chosenSprint;
    }

    public void setChosenSprint(WorkItem.sprintEnum chosenSprint) {
        this.chosenSprint = chosenSprint;
    }

    // Reports
    public HashMap<User, Integer> totalPlannedHoursPerMember() {
        HashMap<User, Integer> sumHoursPerUser = new HashMap<>();
        //implementation
        return sumHoursPerUser;
    }

    public Map<WorkItem.statusEnum, Integer> workItemStatusDistribution() {
        Map<WorkItem.statusEnum, Integer> distribution = new HashMap<>();
        //implementation
        return distribution;
    }

    public List<WorkItem> bugsFoundInVersion() {
        List<WorkItem> bugsFound = new ArrayList<>();
        //implementation
        return bugsFound;
    }

    public List<WorkItem> bugsSolvedInVersion() {
        List<WorkItem> bugsSolved = new ArrayList<>();
        //implementation
        return bugsSolved;
    }

    public List<WorkItem> exceedingEstimations() {
        List<WorkItem> exceedings = new ArrayList<>();
        //implementation
        return exceedings;
    }

}
