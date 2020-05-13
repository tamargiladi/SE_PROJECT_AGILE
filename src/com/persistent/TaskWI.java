package com.persistent;

public class TaskWI extends WorkItem {

    protected priorityEnum priority;
    protected User owner;
    protected Team team;
    protected sprintEnum sprint;
    protected Integer estimate;
    protected Integer timeSpent;
    protected String targetVersion;
    protected Integer storyID;

    public TaskWI(typeEnum type) {
        super(type);
    }

    public priorityEnum getPriority() {
        return priority;
    }

    public void setPriority(priorityEnum priority) {
        this.priority = priority;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
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
