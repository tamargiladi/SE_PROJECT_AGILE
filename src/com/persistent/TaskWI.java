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

    public void updateWorkItem(String summary, WorkItem.statusEnum status, String description, WorkItem.priorityEnum priority, User owner,
                             Integer epicID, Team team, WorkItem.sprintEnum sprint, Integer estimate, Integer timeSpent, String targetVersion,
                             Integer storyID, String foundVersion)
    {
        super.updateWorkItem(summary, status, description, priority, owner, epicID, team, sprint, estimate, timeSpent, targetVersion, storyID, foundVersion);
        this.priority = priority;
        this.owner = owner;
        this.team = team;
        this.sprint = sprint;
        this.estimate = estimate;
        this.timeSpent = timeSpent;
        this.targetVersion = targetVersion;
        this.storyID = storyID;
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
