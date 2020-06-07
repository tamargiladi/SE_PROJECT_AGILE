package com.business;

import com.persistent.User;
import com.persistent.WorkItem;
import com.business.WorkItemManager;
import com.presentation.MainUserInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportGenerator {

    private String chosenVersion;
    private WorkItem.sprintEnum chosenSprint;
    private HashMap<Integer, WorkItem> hashMap = MainUserInterface.WIManager.workItems;

    public ReportGenerator() {
    }

    public boolean isActionPermitted() {
        UserManager userManager = UserManager.getInstance();
        if (userManager.loggedInUser.getPermissionLevel() == User.PermissionLevel.member)
            return false;
        return true;
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
    public HashMap<String, Integer> totalPlannedHoursPerMember() {
        HashMap<String, Integer> sumHoursPerUser = new HashMap<>(); //username, sum of number of estimated hours

        if (isActionPermitted() == false) {
            System.out.println("Report generator action declined: Action not permitted");
            return sumHoursPerUser;
        }

        for (Map.Entry entry : hashMap.entrySet()) {
            WorkItem wi = (WorkItem) entry.getValue();
            if (wi.getSprint() == chosenSprint && wi.getEstimate() != null) {
                if (sumHoursPerUser.containsKey(wi.getOwner()))
                    sumHoursPerUser.put(wi.getOwner(), sumHoursPerUser.get(wi.getOwner()) + wi.getEstimate());
                else
                    sumHoursPerUser.put(wi.getOwner(), wi.getEstimate());
            }
        }
        return sumHoursPerUser;
    }

    public HashMap<WorkItem.statusEnum, Integer> workItemStatusDistribution() {
        HashMap<WorkItem.statusEnum, Integer> distribution = new HashMap<>();
        distribution.put(WorkItem.statusEnum.Done, 0);
        distribution.put(WorkItem.statusEnum.InProgress, 0);
        distribution.put(WorkItem.statusEnum.New, 0);
        WorkItem.statusEnum status = null;

        if (isActionPermitted() == false) {
            System.out.println("Report generator action declined: Action not permitted");
            return distribution;
        }

        for (Map.Entry entry : hashMap.entrySet()) {
            WorkItem wi = (WorkItem) entry.getValue();
            if (wi.getSprint() == chosenSprint) {
                status = wi.getStatus();
                if (status == WorkItem.statusEnum.Done)
                    distribution.put(status, distribution.get(status) + 1);
                else if (status == WorkItem.statusEnum.InProgress)
                    distribution.put(status, distribution.get(status) + 1);
                else if (status == WorkItem.statusEnum.New)
                    distribution.put(status, distribution.get(status) + 1);
            }
        }
        return distribution;
    }

    public List<WorkItem> bugsFoundInVersion() {
        List<WorkItem> bugsFound = new ArrayList<>();

        if (isActionPermitted() == false) {
            System.out.println("Report generator action declined: Action not permitted");
            return bugsFound;
        }

        for (Map.Entry entry : hashMap.entrySet()) {
            WorkItem wi = (WorkItem) entry.getValue();
            if (wi.getFoundVersion() != null && wi.getFoundVersion().equals(chosenVersion) && wi.getType() == WorkItem.typeEnum.Bug)
                    bugsFound.add(wi);
        }
        return bugsFound;
    }

    public List<WorkItem> bugsSolvedInVersion() {
        List<WorkItem> bugsSolved = new ArrayList<>();

        if (isActionPermitted() == false) {
            System.out.println("Report generator action declined: Action not permitted");
            return bugsSolved;
        }

        for (Map.Entry entry : hashMap.entrySet()) {
            WorkItem wi = (WorkItem) entry.getValue();
            if (wi.getTargetVersion() != null && wi.getTargetVersion().equals(chosenVersion) && wi.getStatus() == WorkItem.statusEnum.Done && wi.getType() == WorkItem.typeEnum.Bug)
                bugsSolved.add(wi);
        }
        return bugsSolved;
    }

    public List<WorkItem> exceedingEstimations() {
        List<WorkItem> exceedingEst = new ArrayList<>();

        if (isActionPermitted() == false) {
            System.out.println("Report generator action declined: Action not permitted");
            return exceedingEst;
        }

        for (Map.Entry entry : hashMap.entrySet()) {
            WorkItem wi = (WorkItem) entry.getValue();
            if (wi.getTimeSpent() != null && wi.getEstimate() != null && wi.getTimeSpent() > wi.getEstimate())
                exceedingEst.add(wi);
        }
        return exceedingEst;
    }

}
