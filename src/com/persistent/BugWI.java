package com.persistent;

public class BugWI extends TaskWI {

    private String foundVersion;

    public BugWI() {
        super();
        this.type = typeEnum.Bug;
        foundVersion = null;
    }

    public void updateWorkItem(String summary, WorkItem.statusEnum status, String description, WorkItem.priorityEnum priority, User owner,
                             Integer epicID, Team team, WorkItem.sprintEnum sprint, Integer estimate, Integer timeSpent, String targetVersion,
                             Integer storyID, String foundVersion)
    {
        super.updateWorkItem(summary, status, description, priority, owner, epicID, team, sprint, estimate, timeSpent, targetVersion, storyID, foundVersion);
        this.foundVersion = foundVersion;
    }


    public String getFoundVersion() {
        return foundVersion;
    }

    public void setFoundVersion(String foundVersion) {
        this.foundVersion = foundVersion;
    }
}
