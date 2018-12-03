package com.cmps121.rideplanner;

import java.util.Map;

public class Event {
    public String eventName;
    public String eventDescription;
    public String eventLocation;
    public Map<String, User> attendees;

    public Event(String eventName, String eventDescription, String eventLocation, Map<String, User> attendees) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventLocation = eventLocation;
        this.attendees = attendees;
    }
}
