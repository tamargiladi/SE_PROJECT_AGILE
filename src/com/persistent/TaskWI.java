package com.persistent;

public class TaskWI extends WorkItem {

    protected priorityEnum priority;
    protected String owner;
    protected String team;
    protected sprintEnum sprint;
    protected Integer estimate;
    protected Integer timeSpent;
    protected String targetVersion;
    protected Integer storyID;

    public TaskWI() {
        super();
        this.type = typeEnum.Task;
        priority = priorityEnum.Unassigned;
        sprint = sprintEnum.Unassigned;
        estimate = null;
        timeSpent = null;
        targetVersion = null;
        storyID = null;
    }


    public priorityEnum getPriority() {
        return priority;
    }

    public void setPriority(priorityEnum priority) {
        this.priority = priority;
    }


    public String  getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }


    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }


    public sprintEnum getSprint() {
        return sprint;
    }

    public void setSprint(sprintEnum sprint) {
        this.sprint = sprint;
    }


    public Integer getEstimate() {
        return estimate;
    }

    public void setEstimate(Integer estimate) {
        this.estimate = estimate;
    }


    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }


    public String getTargetVersion() {
        return targetVersion;
    }

    public void setTargetVersion(String targetVersion) {
        this.targetVersion = targetVersion;
    }


    public Integer getStoryID() {
        return storyID;
    }

    public void setStoryID(Integer storyID) {
        this.storyID = storyID;
    }

}
