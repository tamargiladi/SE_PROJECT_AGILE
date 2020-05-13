package com.persistent;

public class BugWI extends TaskWI {

    private String foundVersion;

    public BugWI(typeEnum type) {
        super(type);
    }

    public String getFoundVersion() {
        return foundVersion;
    }

    public void setFoundVersion(String foundVersion) {
        this.foundVersion = foundVersion;
    }
}
