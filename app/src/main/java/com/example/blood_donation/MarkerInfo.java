package com.example.blood_donation;

public class MarkerInfo {
    private String name;
    private String bloodGroup;
    private String distance;
    private String phoneNumber;

    // Constructor
    public MarkerInfo(String name, String bloodGroup, String distance, String phoneNumber) {
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.distance = distance;
        this.phoneNumber = phoneNumber;
    }

    // Getters
    public String getName() { return name; }
    public String getBloodGroup() { return bloodGroup; }
    public String getDistance() { return distance; }
    public String getPhoneNumber() { return phoneNumber;}

}
