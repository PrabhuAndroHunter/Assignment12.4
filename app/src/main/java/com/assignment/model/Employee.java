package com.assignment.model;

/**
 * Created by prabhu on 24/1/18.
 */

public class Employee {
    private int id;
    private String name;
    private String dob;
    private String phoneNumber;

    public Employee(int id, String name, String dob, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }

    public String getphoneNumber() {
        return phoneNumber;
    }
}
