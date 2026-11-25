# 🗳️ Voting Management System (Java)

A voting management system built in Java as university coursework at the Informatics Institute of Technology (IIT), Sri Lanka.

It loads voters, candidates and ballots from CSV files, validates voter IDs with a check-digit algorithm, and lets you search results by district and export them to a text file.

## ✨ Features

- 📂 **CSV data**: works with `voter.csv`, `candidate.csv` and `ballots.csv`
- ✅ **ID validation**: check-digit validator for voter IDs, covered by JUnit tests
- 🔎 **District search**: look up results by district
- 📝 **Export to text**: save district search results to a `.txt` file

## 🛠️ Built with

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat-square&logo=openjdk&logoColor=white) ![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat-square&logo=apachemaven&logoColor=white) ![JUnit](https://img.shields.io/badge/JUnit-25A162?style=flat-square&logo=junit5&logoColor=white)

## 🚀 Run it

```bash
git clone https://github.com/madhushaney14/voting-management-system-java.git
cd voting-management-system-java
mvn compile
mvn test        # run the JUnit tests
```

Then run the main class from `src` in your IDE (e.g. IntelliJ IDEA). Sample CSV files are included in the project root.

## 🔁 See also

The same system rebuilt in Python: [voting-management-system-python](https://github.com/madhushaney14/voting-management-system-python)
