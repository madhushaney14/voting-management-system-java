package com.vms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileImporter {

    private List<InvalidRow> invalidRows = new ArrayList<>();

    public List<InvalidRow> getInvalidRows() {
        return invalidRows;
    }

    public List<Voter> importVoters(String filepath) {
        List<Voter> voters = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length != 3) {
                    invalidRows.add(new InvalidRow(line, "Wrong number of columns", "voter"));
                    continue;
                }

                String voterId = row[0].trim();
                String district = row[1].trim();
                String ageStr = row[2].trim();

                if (!CheckDigitValidator.isValidId(voterId)) {
                    invalidRows.add(new InvalidRow(line, "Invalid voter ID", "voter"));
                    continue;
                }

                boolean alreadyExists = false;
                for (Voter v : voters) {
                    if (v.getVoterId().equals(voterId)) {
                        alreadyExists = true;
                        break;
                    }
                }
                if (alreadyExists) {
                    invalidRows.add(new InvalidRow(line, "Duplicate voter ID", "voter"));
                    continue;
                }

                try {
                    int age = Integer.parseInt(ageStr);
                    voters.add(new Voter(voterId, district, age));
                } catch (NumberFormatException e) {
                    invalidRows.add(new InvalidRow(line, "Invalid age", "voter"));
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
                    invalidRows.add(new InvalidRow(line, "Wrong number of columns", "candidate"));
                    continue;
                }

                String candidateId = row[0].trim();
                String name = row[1].trim();
                String seat = row[2].trim();

                if (!CheckDigitValidator.isValidId(candidateId)) {
                    invalidRows.add(new InvalidRow(line, "Invalid candidate ID", "candidate"));
                    continue;
                }

                candidates.add(new Candidate(candidateId, name, seat));
            }
        } catch (IOException e) {
            System.out.println("Error reading candidate file: " + e.getMessage());
        }

        return candidates;
    }

    public List<Ballot> importBallots(String filepath, List<Candidate> candidates) {
        List<Ballot> ballots = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length != 6) {
                    invalidRows.add(new InvalidRow(line, "Wrong number of columns", "ballot"));
                    continue;
                }

                String date = row[0].trim();
                String voterId = row[1].trim();
                String candidateId = row[2].trim();
                String seat = row[3].trim();
                String ageStr = row[4].trim();
                String district = row[5].trim();

                if (!CheckDigitValidator.isValidId(voterId)) {
                    invalidRows.add(new InvalidRow(line, "Invalid voter ID", "ballot"));
                    continue;
                }

                if (!CheckDigitValidator.isValidId(candidateId)) {
                    invalidRows.add(new InvalidRow(line, "Invalid candidate ID", "ballot"));
                    continue;
                }

                boolean seatMatches = false;
                for (Candidate c : candidates) {
                    if (c.getSeat().equals(seat)) {
                        seatMatches = true;
                        break;
                    }
                }
                if (!seatMatches) {
                    invalidRows.add(new InvalidRow(line, "Invalid seat number", "ballot"));
                    continue;
                }

                if (!isValidDateFormat(date)) {
                    invalidRows.add(new InvalidRow(line, "Invalid date format", "ballot"));
                    continue;
                }

                try {
                    int voterAge = Integer.parseInt(ageStr);
                    ballots.add(new Ballot(date, voterId, candidateId, seat, voterAge, district));
                } catch (NumberFormatException e) {
                    invalidRows.add(new InvalidRow(line, "Invalid age", "ballot"));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading ballot file: " + e.getMessage());
        }

        return ballots;
    }

    private boolean isValidDateFormat(String date) {
        try {
            java.time.LocalDate.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}