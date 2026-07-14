package com.vms;

public class InvalidRow {
    private String rawLine;
    private String reason;
    private String fileType;

    public InvalidRow(String rawLine, String reason, String fileType) {
        this.rawLine = rawLine;
        this.reason = reason;
        this.fileType = fileType;
    }

    public String getRawLine() {
        return rawLine;
    }

    public String getReason() {
        return reason;
    }

    public String getFileType() {
        return fileType;
    }

    @Override
    public String toString() {
        return "[" + fileType + "] " + rawLine + " -- " + reason;
    }
}