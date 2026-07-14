package com.vms;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportGenerator {
    private List<Ballot> ballots;
    private List<Candidate> candidates;

    public ReportGenerator(List<Ballot> ballots, List<Candidate> candidates) {
        this.ballots = ballots;
        this.candidates = candidates;
    }

    public Map<String, Integer> getVotesReceivedReport(String date) {
        Map<String, Integer> report = new HashMap<>();

        for (Ballot ballot : ballots) {
            if (ballot.getDate().equals(date)) {
                String candidateId = ballot.getCandidateId();
                report.put(candidateId, report.getOrDefault(candidateId, 0) + 1);
            }
        }

        return report;
    }

    public Map<String, Double> getAverageAgePerSeat() {
        Map<String, List<Integer>> agesPerSeat = new HashMap<>();

        for (Ballot ballot : ballots) {
            String seat = ballot.getSeat();
            agesPerSeat.putIfAbsent(seat, new ArrayList<>());
            agesPerSeat.get(seat).add(ballot.getVoterAge());
        }

        Map<String, Double> averages = new HashMap<>();
        for (String seat : agesPerSeat.keySet()) {
            List<Integer> ages = agesPerSeat.get(seat);
            int sum = 0;
            for (int age : ages) {
                sum += age;
            }
            double average = (double) sum / ages.size();
            averages.put(seat, average);
        }

        return averages;
    }

    public List<Ballot> searchByDistrict(String query) {
        List<Ballot> results = new ArrayList<>();

        String searchTerm = query;
        boolean wildcard = query.endsWith("*");
        if (wildcard) {
            searchTerm = query.substring(0, query.length() - 1);
        }

        for (Ballot ballot : ballots) {
            String district = ballot.getDistrict();
            if (wildcard) {
                if (district.toLowerCase().startsWith(searchTerm.toLowerCase())) {
                    results.add(ballot);
                }
            } else {
                if (district.equalsIgnoreCase(searchTerm)) {
                    results.add(ballot);
                }
            }
        }

        return results;
    }
}