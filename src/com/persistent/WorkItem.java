package com.persistent;
import com.business.WorkItemManager;

public abstract class WorkItem {

    protected Integer id;
    protected typeEnum type;
    protected String summary;
    protected statusEnum status;
    protected String description;

    public WorkItem() {
        this.id = WorkItemManager.getAvailableId();
        this.type = typeEnum.Epic;//type;
        this.summary = null;
        this.status = statusEnum.New;
        this.description = null;
    }


    public enum typeEnum {
        Epic,
        Story,
        Task,
        Bug
    }

    public enum statusEnum {
        New,
        InProgress,
        Done
    }

    public enum priorityEnum {
        Unassigned,
        Low,
        Medium,
        High
    }

    public enum sprintEnum {
        Unassigned,
        Backlog,
        Jan20,
        Feb20,
        Mar20,
        Apr20,
        May20,
        Jun20,
        Jul20,
        Aug20,
        Sep20,
        Oct20,
        Nov20,
        Dec20
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }


    public statusEnum getStatus() {
        return status;
    }

    public void setStatus(statusEnum status) {
        this.status = status;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public typeEnum getType() {
        return type;
    }

    public void setType(typeEnum type) {
        this.type = type;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // Abstract

    public priorityEnum getPriority() {
        return null;
    }
    public void setPriority(priorityEnum priority) {
    }

    public String getOwner() {
        return null;
    }
    public void setOwner(String owner) {
    }

    public String getTeam() {
        return null;
    }
    public void setTeam(String team) {
    }


    public sprintEnum getSprint() {
        return null;
    }
    public void setSprint(sprintEnum sprint) {
    }

    public Integer getEstimate() {
        return null;
    }
    public void setEstimate(Integer estimate) {
    }

    public Integer getTimeSpent() {
        return null;
    }
    public void setTimeSpent(Integer timeSpent) {
    }

    public String getTargetVersion() {
        return null;
    }
    public void setTargetVersion(String targetVersion) {
    }

    public Integer getStoryID() {
        return null;
    }
    public void setStoryID(Integer storyID) {
    }

    public String getFoundVersion() {
        return null;
    }
    public void setFoundVersion(String foundVersion) {
    }

    public Integer getEpicID() {
        return null;
    }

    public void setEpicID(Integer epicID) { }

}
