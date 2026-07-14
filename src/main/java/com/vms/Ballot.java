package com.vms;

public class Ballot {
    private String date;
    private String voterId;
    private String candidateId;
    private String seat;
    private int voterAge;
    private String district;

    public Ballot(String date, String voterId, String candidateId, String seat, int voterAge, String district) {
        this.date = date;
        this.voterId = voterId;
        this.candidateId = candidateId;
        this.seat = seat;
        this.voterAge = voterAge;
        this.district = district;
    }

    public String getDate() {
        return date;
    }

    public String getVoterId() {
        return voterId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public String getSeat() {
        return seat;
    }

    public int getVoterAge() {
        return voterAge;
    }

    public String getDistrict() {
        return district;
    }
}