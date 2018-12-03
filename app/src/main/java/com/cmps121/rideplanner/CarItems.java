package com.cmps121.rideplanner;

import java.util.ArrayList;
import java.util.List;

public class CarItems {
    String driverName;
    String passenger1;
    String passenger2;
    String passenger3;
    String passenger4;
    ArrayList<String> passengers = new ArrayList<>();

    public CarItems(String driverName, String passenger1, String passenger2, String passenger3, String passenger4) {
        this.driverName = driverName;
        this.passenger1 = passenger1;
        this.passenger2 = passenger2;
        this.passenger3 = passenger3;
        this.passenger4 = passenger4;
    }

    public CarItems(String driverName, ArrayList<String> passengers) {
        this.driverName = driverName;
        this.passenger1 = passengers.get(0);
        this.passenger2 = passengers.get(1);
        this.passenger3 = passengers.get(2);
        this.passenger4 = passengers.get(3);
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setPassenger1(String passenger1) {
        this.passenger1 = passenger1;
    }

    public void setPassenger2(String passenger2) {
        this.passenger2 = passenger2;
    }

    public void setPassenger3(String passenger3) {
        this.passenger3 = passenger3;
    }

    public void setPassenger4(String passenger4) {
        this.passenger4 = passenger4;
    }

    public void setPassengers(ArrayList<String> passengers) {
        this.passengers = passengers;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getPassenger1() {
        return passenger1;
    }

    public String getPassenger2() {
        return passenger2;
    }

    public String getPassenger3() {
        return passenger3;
    }

    public String getPassenger4() {
        return passenger4;
    }
}
