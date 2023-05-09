package com.example.todoapp.dataModel.AuditLog;

import java.time.LocalDate;

public class AuditLogItem {
    private static int logId;
    private LocalDate dateTime;
    private String details;

    public AuditLogItem(LocalDate dateTime, String details)
    {
        logId++;
        this.dateTime = dateTime;
        this.details = details;
    }

    public static int getLogId() {
        return logId;
    }

    public LocalDate getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
