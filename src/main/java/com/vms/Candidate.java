package com.vms;

public class Candidate {
    private String candidateId;
    private String name;
    private String seat;

    public Candidate(String candidateId, String name, String seat) {
        this.candidateId = candidateId;
        this.name = name;
        this.seat = seat;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public String getName() {
        return name;
    }

    public String getSeat() {
        return seat;
    }
}