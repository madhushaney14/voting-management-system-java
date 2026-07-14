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

        ListView<InvalidRow> invalidList = new ListView<>();
        invalidList.setPrefHeight(150);

        Label editLabel = new Label("Selected row (edit below, comma-separated):");
        TextField editField = new TextField();
        Button fixButton = new Button("Re-validate and Add");
        Label fixResultLabel = new Label();

        invalidList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                editField.setText(newVal.getRawLine());
            }
        });

        importButton.setOnAction(e -> {
            FileImporter importer = new FileImporter();
            voters = importer.importVoters("voter.csv");
            candidates = importer.importCandidates("candidate.csv");
            ballots = importer.importBallots("ballots.csv", candidates);
            reportGenerator = new ReportGenerator(ballots, candidates);

            invalidList.getItems().setAll(importer.getInvalidRows());

            statusLabel.setText("Loaded " + voters.size() + " voters, " +
                    candidates.size() + " candidates, " + ballots.size() + " ballots. " +
                    importer.getInvalidRows().size() + " invalid rows found.");
        });

        fixButton.setOnAction(e -> {
            InvalidRow selected = invalidList.getSelectionModel().getSelectedItem();
            if (selected == null) {
                fixResultLabel.setText("Please select a row to fix first.");
                return;
            }

            String[] parts = editField.getText().split(",");

            if (selected.getFileType().equals("voter") && parts.length == 3) {
                String voterId = parts[0].trim();
                if (CheckDigitValidator.isValidId(voterId)) {
                    try {
                        int age = Integer.parseInt(parts[2].trim());
                        voters.add(new Voter(voterId, parts[1].trim(), age));
                        invalidList.getItems().remove(selected);
                        fixResultLabel.setText("Row fixed and added successfully.");
                    } catch (NumberFormatException ex) {
                        fixResultLabel.setText("Age must be numeric.");
                    }
                } else {
                    fixResultLabel.setText("Voter ID still invalid.");
                }
            } else if (selected.getFileType().equals("candidate") && parts.length == 3) {
                String candidateId = parts[0].trim();
                if (CheckDigitValidator.isValidId(candidateId)) {
                    candidates.add(new Candidate(candidateId, parts[1].trim(), parts[2].trim()));
                    invalidList.getItems().remove(selected);
                    fixResultLabel.setText("Row fixed and added successfully.");
                } else {
                    fixResultLabel.setText("Candidate ID still invalid.");
                }
            } else {
                fixResultLabel.setText("Could not re-validate this row type.");
            }
        });

        box.getChildren().addAll(statusLabel, importButton,
                new Label("Invalid rows found during import:"), invalidList,
                editLabel, editField, fixButton, fixResultLabel);
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