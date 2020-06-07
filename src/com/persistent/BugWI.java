package com.persistent;

public class BugWI extends TaskWI {

    private String foundVersion;

    public BugWI() {
        super();
        this.type = typeEnum.Bug;
        foundVersion = null;
    }

    public String getFoundVersion() {
        return foundVersion;
    }

    public void setFoundVersion(String foundVersion) {
        this.foundVersion = foundVersion;
    }
}
