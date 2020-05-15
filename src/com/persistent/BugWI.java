package com.persistent;

public class BugWI extends TaskWI {

    private String foundVersion;

    public BugWI() {
        super();
        this.type = typeEnum.Bug;
        foundVersion = null;
    }

    public void saveWorkItem(String summary,statusEnum status, String description, priorityEnum priority, User owner,
                             Team team, sprintEnum sprint, Integer estimate, Integer timeSpent, String targetVersion,
                             Integer storyID, String foundVersion)
    {
        this.foundVersion = foundVersion;
    }


    public String getFoundVersion() {
        return foundVersion;
    }

    public void setFoundVersion(String foundVersion) {
        this.foundVersion = foundVersion;
    }
}
