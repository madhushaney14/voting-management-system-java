package com.vms;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class MainApp extends Application {

    private List<Voter> voters;
    private List<Candidate> candidates;
    private List<Ballot> ballots;
    private ReportGenerator reportGenerator;

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        Tab importTab = new Tab("Import Data", buildImportScreen());
        importTab.setClosable(false);

        Tab votesTab = new Tab("Votes Received Report", buildVotesReportScreen());
        votesTab.setClosable(false);

        Tab ageTab = new Tab("Average Age Per Seat", buildAverageAgeScreen());
        ageTab.setClosable(false);

        Tab searchTab = new Tab("District Search", buildDistrictSearchScreen());
        searchTab.setClosable(false);

        tabPane.getTabs().addAll(importTab, votesTab, ageTab, searchTab);

        Scene scene = new Scene(tabPane, 700, 500);
        primaryStage.setTitle("VMS - Java Reporting Module");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox buildImportScreen() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));

        Label statusLabel = new Label("Click 'Import' to load voter.csv, candidate.csv, and ballots.csv");
        Button importButton = new Button("Import Data");

        TextArea logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(300);

        importButton.setOnAction(e -> {
            FileImporter importer = new FileImporter();
            voters = importer.importVoters("voter.csv");
            candidates = importer.importCandidates("candidate.csv");
            ballots = importer.importBallots("ballots.csv");
            reportGenerator = new ReportGenerator(ballots, candidates);

            statusLabel.setText("Loaded " + voters.size() + " voters, " +
                    candidates.size() + " candidates, " + ballots.size() + " ballots.");
            logArea.setText("Import complete.\n");
        });

        box.getChildren().addAll(statusLabel, importButton, logArea);
        return box;
    }

    private VBox buildVotesReportScreen() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));

        TextField dateField = new TextField();
        dateField.setPromptText("Enter date (yyyy-mm-dd)");
        Button generateButton = new Button("Generate Report");
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(300);

        generateButton.setOnAction(e -> {
            if (reportGenerator == null) {
                resultArea.setText("Please import data first.");
                return;
            }
            Map<String, Integer> report = reportGenerator.getVotesReceivedReport(dateField.getText());
            StringBuilder sb = new StringBuilder();
            for (String candidateId : report.keySet()) {
                sb.append("Candidate ").append(candidateId).append(": ")
                        .append(report.get(candidateId)).append(" votes\n");
            }
            resultArea.setText(sb.toString());
        });

        box.getChildren().addAll(new Label("Date:"), dateField, generateButton, resultArea);
        return box;
    }

    private VBox buildAverageAgeScreen() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));

        Button generateButton = new Button("Calculate Average Age Per Seat");
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(300);

        generateButton.setOnAction(e -> {
            if (reportGenerator == null) {
                resultArea.setText("Please import data first.");
                return;
            }
            Map<String, Double> averages = reportGenerator.getAverageAgePerSeat();
            StringBuilder sb = new StringBuilder();
            for (String seat : averages.keySet()) {
                sb.append("Seat ").append(seat).append(": ")
                        .append(String.format("%.2f", averages.get(seat))).append(" years\n");
            }
            resultArea.setText(sb.toString());
        });

        box.getChildren().addAll(generateButton, resultArea);
        return box;
    }

    private VBox buildDistrictSearchScreen() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));

        TextField searchField = new TextField();
        searchField.setPromptText("Enter district name (use * for wildcard, e.g. Col*)");
        Button searchButton = new Button("Search");
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(300);

        searchButton.setOnAction(e -> {
            if (reportGenerator == null) {
                resultArea.setText("Please import data first.");
                return;
            }
            List<Ballot> results = reportGenerator.searchByDistrict(searchField.getText());
            StringBuilder sb = new StringBuilder();
            for (Ballot ballot : results) {
                sb.append(ballot.getDate()).append(" | ").append(ballot.getVoterId())
                        .append(" | ").append(ballot.getDistrict()).append("\n");
            }
            resultArea.setText(sb.toString());
        });

        box.getChildren().addAll(new Label("District:"), searchField, searchButton, resultArea);
        return box;
    }

    public static void main(String[] args) {
        launch(args);
    }
}