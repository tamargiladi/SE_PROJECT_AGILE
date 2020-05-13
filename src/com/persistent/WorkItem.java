package com.persistent;
import com.business.WorkItemManager;

public abstract class WorkItem {

    protected Integer id;
    protected typeEnum type;
    protected String summary;
    protected statusEnum status;
    protected String description;

    public WorkItem(typeEnum type) {
        this.id = WorkItemManager.getAvailableId();
        this.type = type;
        this.summary = "";
        this.status = statusEnum.New;
        this.description = "";
    }


    enum typeEnum {
        Epic,
        Story,
        Task,
        Bug
    }

    enum statusEnum {
        New,
        InProgress,
        Done
    }

    enum priorityEnum {
        Unassigned,
        Low,
        Medium,
        High
    }

    enum sprintEnum {
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

}
