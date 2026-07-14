package com.vms;

public class Voter {
    private String voterId;
    private String district;
    private int age;

    public Voter(String voterId, String district, int age) {
        this.voterId = voterId;
        this.district = district;
        this.age = age;
    }

    public String getVoterId() {
        return voterId;
    }

    public String getDistrict() {
        return district;
    }

    public int getAge() {
        return age;
    }
}