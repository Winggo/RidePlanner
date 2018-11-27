package com.cmps121.rideplanner;

import com.google.firebase.database.FirebaseDatabase;

public class EventInviteListItem {
    public String groupName;
    public String eventName;
    public String groupCode;

    public EventInviteListItem(String groupName, String groupCode, String eventName) {
        this.groupName = groupName;
        this.eventName = eventName;
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getEventName() {
        return eventName;
    }

    public String getGroupCode() {
        return groupCode;
    }
}