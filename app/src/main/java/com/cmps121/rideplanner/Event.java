package com.cmps121.rideplanner;

import java.util.Map;

public class Event {
    public String eventName;
    public String eventDescription;
    public Map<String, Boolean> attendees;

    public Event(String eventName, String eventDescription, Map<String, Boolean> attendees) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.attendees = attendees;
    }
}
