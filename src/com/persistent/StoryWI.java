package com.persistent;

public class StoryWI extends WorkItem {

    private priorityEnum priority;
    private User owner;
    private Integer epicID;

    public StoryWI(typeEnum type) {
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

    public Integer getEpicID() {
        return epicID;
    }

    public void setEpicID(Integer epicID) {
        this.epicID = epicID;
    }
}
