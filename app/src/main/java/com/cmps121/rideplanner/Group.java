package com.cmps121.rideplanner;

import java.util.ArrayList;

public class Group {

    private String groupName;
    private String groupCode;

    public Group (String groupName, String groupCode) {
        this.groupName = groupName;
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }
    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupName(String name) {
        this.groupName = name;
    }
    public void setGroupCode(String code) {
        this.groupCode = code;
    }
}
