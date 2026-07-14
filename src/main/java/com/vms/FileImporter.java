package com.vms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileImporter {

    public List<Voter> importVoters(String filepath) {
        List<Voter> voters = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length != 3) {
                    System.out.println("Warning: Skipping malformed voter row: " + line);
                    continue;
                }

                String voterId = row[0].trim();
                String district = row[1].trim();
                String ageStr = row[2].trim();

                if (!CheckDigitValidator.isValidId(voterId)) {
                    System.out.println("Warning: Invalid voter ID: " + voterId);
                    continue;
                }

                try {
                    int age = Integer.parseInt(ageStr);
                    voters.add(new Voter(voterId, district, age));
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Invalid age for voter: " + voterId);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading voter file: " + e.getMessage());
        }

        return voters;
    }

    public List<Candidate> importCandidates(String filepath) {
        List<Candidate> candidates = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length != 3) {
                    System.out.println("Warning: Skipping malformed candidate row: " + line);
                    continue;
                }

                String candidateId = row[0].trim();
                String name = row[1].trim();
                String seat = row[2].trim();

                if (!CheckDigitValidator.isValidId(candidateId)) {
                    System.out.println("Warning: Invalid candidate ID: " + candidateId);
                    continue;
                }

                candidates.add(new Candidate(candidateId, name, seat));
            }
        } catch (IOException e) {
            System.out.println("Error reading candidate file: " + e.getMessage());
        }

        return candidates;
    }

    public List<Ballot> importBallots(String filepath) {
        List<Ballot> ballots = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length != 6) {
                    System.out.println("Warning: Skipping malformed ballot row: " + line);
                    continue;
                }

                String date = row[0].trim();
                String voterId = row[1].trim();
                String candidateId = row[2].trim();
                String seat = row[3].trim();
                String ageStr = row[4].trim();
                String district = row[5].trim();

                try {
                    int voterAge = Integer.parseInt(ageStr);
                    ballots.add(new Ballot(date, voterId, candidateId, seat, voterAge, district));
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Invalid age in ballot row: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading ballot file: " + e.getMessage());
        }

        return ballots;
    }
}