package com.example.todoapp.dataModel.AuditLog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class AuditLogData {
    private static AuditLogData instance = new AuditLogData();
    private static String fileName ="AuditLogData.txt";
    private ObservableList<AuditLogItem> auditLogItems;
    private DateTimeFormatter formatter;

    public static AuditLogData getInstance()
    {
        return instance;
    }
    public AuditLogData()
    {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public ObservableList<AuditLogItem> getAuditLogItems() {
        return auditLogItems;
    }
    public void loadAuditLogItems () throws IOException
    {
        auditLogItems = FXCollections.observableArrayList();
        Path path = Paths.get(fileName);//nio
        BufferedReader br = Files.newBufferedReader(path);
        String input;
        try {
            while((input = br.readLine()) != null) { // whichever line it reads should not be null
                String[] itemPieces = input.split("\t");
                int auditId = AuditLogItem.getLogId();
                String auditDate = itemPieces[1];
                String auditDetails = itemPieces[2];
                LocalDate date = LocalDate.parse(auditDate,formatter);
                AuditLogItem item = new AuditLogItem(date,auditDetails);
                auditLogItems.add(item);
            }
        }
        finally {
            if(br!=null)
            {
                br.close();
            }

        }
    }
    public void storeAuditLogs() throws IOException
    {
        Path path = Paths.get(fileName);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try
        {
            Iterator<AuditLogItem> iter = auditLogItems.iterator();
            while(iter.hasNext())
            {
                AuditLogItem item = iter.next();
                bw.write(String.format("%s\t%s\t%s",
                        AuditLogItem.getLogId(),
                        item.getDateTime().format(formatter),
                        item.getDetails()));
                bw.newLine();
            }
        }
        finally {
            if(bw!=null)
                bw.close();
        }
    }

    public void addAuditLogItem(AuditLogItem item)
    {
        auditLogItems.add(item);
    }
}
