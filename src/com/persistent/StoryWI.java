package com.persistent;

public class StoryWI extends WorkItem {

    private priorityEnum priority;
    private User owner;
    private Integer epicID;

    public StoryWI() {
        super();
        this.type = typeEnum.Story;
        this.priority = priorityEnum.Unassigned;
        this.epicID = null;
    }

    public void saveWorkItem(String summary,statusEnum status, String description, priorityEnum priority, User owner, Integer epicID) {
        this.priority = priority;
        this.owner = owner;
        this.epicID = epicID;
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
