package com.persistent;

import com.business.WorkItemManager;

public class WorkItemBuilder {

    private Integer id;
    private WorkItem.typeEnum type;
    private String summary;
    private WorkItem.statusEnum status;
    private String description;
    private WorkItem.priorityEnum priority;
    private String owner;
    private Integer epicID;
    private String team;
    private WorkItem.sprintEnum sprint;
    private Integer estimate;
    private Integer timeSpent;
    private String targetVersion;
    private Integer storyID;
    private String foundVersion;

    public WorkItem build(WorkItem.typeEnum type, WorkItem workItem) {
        boolean isNew = false;
        if (workItem == null) {
            isNew = true;
            if (type == WorkItem.typeEnum.Epic) { workItem = new EpicWI(); }
            else if (type == WorkItem.typeEnum.Story) { workItem = new StoryWI(); }
            else if (type == WorkItem.typeEnum.Task) { workItem = new TaskWI(); }
            else { workItem = new BugWI(); } //bug
        }

        workItem.setSummary(this.summary);
        workItem.setStatus(this.status);
        workItem.setDescription(this.description);
        workItem.setPriority(this.priority);
        workItem.setOwner(this.owner);
        workItem.setEpicID(this.epicID);
        workItem.setTeam(this.team);
        workItem.setSprint(this.sprint);
        workItem.setEstimate(this.estimate);
        workItem.setTimeSpent(this.timeSpent);
        workItem.setTargetVersion(this.targetVersion);
        workItem.setStoryID(this.storyID);
        workItem.setFoundVersion(this.foundVersion);

        WorkItemManager.getInstance().addWorkItemToHashMap(workItem, isNew);
        return workItem;
    }

    public static WorkItemBuilder builder() {
        return new WorkItemBuilder();
    }

    public WorkItemBuilder withSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public WorkItemBuilder withStatus(WorkItem.statusEnum status) {
        this.status = status;
        return this;
    }

    public WorkItemBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public WorkItemBuilder withPriority(WorkItem.priorityEnum priority) {
        this.priority = priority;
        return this;
    }

    public WorkItemBuilder withOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public WorkItemBuilder withEpicID(Integer epicID) {
        this.epicID = epicID;
        return this;
    }

    public WorkItemBuilder withTeam(String team) {
        this.team = team;
        return this;
    }

    public WorkItemBuilder withSprint(WorkItem.sprintEnum sprint) {
        this.sprint = sprint;
        return this;
    }

    public WorkItemBuilder withEstimate(Integer estimate) {
        this.estimate = estimate;
        return this;
    }

    public WorkItemBuilder withTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
        return this;
    }

    public WorkItemBuilder withTargetVersion(String targetVersion) {
        this.targetVersion = targetVersion;
        return this;
    }

    public WorkItemBuilder withStoryID(Integer storyID) {
        this.storyID = storyID;
        return this;
    }

    public WorkItemBuilder withFoundVersion(String foundVersion) {
        this.foundVersion = foundVersion;
        return this;
    }

}
