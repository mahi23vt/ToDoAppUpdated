package com.example.todoapp.dataModel;

import com.example.todoapp.dataModel.AuditLog.AuditLogData;
import com.example.todoapp.dataModel.AuditLog.AuditLogItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class ToDoData {
    private static ToDoData instance = new ToDoData();
    private static String fileName = "toDoItemsList.txt";
    private ObservableList<ToDoItem> toDoItems;
    private DatePicker datePicker;
    AuditLogData logData = new AuditLogData();

    private DateTimeFormatter formatter;
    public static ToDoData getInstance()
    {
        return instance;
    }
    private ToDoData ()
    {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }
    public ObservableList<ToDoItem> getToDoItems() {
        return toDoItems;
    }
    public void loadToDoItems () throws IOException
    {
        toDoItems= FXCollections.observableArrayList();
        Path path= Paths.get(fileName); // nio
        BufferedReader br = Files.newBufferedReader(path);
        String input;
        try
        {
            while((input = br.readLine()) != null)
            {
                String[] itemPieces = input.split("\t");
                String itemDescription = itemPieces[0];
                String details = itemPieces[1];
                String dateString = itemPieces[2];
                LocalDate date = LocalDate.parse(dateString, formatter);
                ToDoItem toDoItem = new ToDoItem(itemDescription,details,date);
                toDoItems.add(toDoItem);
            }
        }
        finally {
            if(br!= null)
                br.close();
        }


    }
    public void storeToDoItems()  throws  IOException
    {
        Path path = Paths.get(fileName);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try
        {
            Iterator<ToDoItem> iter = toDoItems.iterator();
            while(iter.hasNext())
            {
                ToDoItem item = iter.next();
                bw.write(String.format("%s\t%s\t%s",
                        item.getShortDescription(),
                        item.getDetails(),
                        item.getDeadLine().format(formatter)));
                bw.newLine();
            }
        }
        finally {
            if(bw!=null)
                bw.close();
        }
    }
    public void addToDoItem(ToDoItem item)
    {
        toDoItems.add(item);
        LocalDate todayDate = datePicker.getValue();
        AuditLogItem auditLogItem = new AuditLogItem(todayDate,"\tAdded the Item: "+item.getDetails());
        logData.addAuditLogItem(auditLogItem);
    }
    public void deleteTodoItem(ToDoItem item) {
        toDoItems.remove(item);
        LocalDate todayDate = datePicker.getValue();
        AuditLogItem auditLogItem = new AuditLogItem(todayDate,"\tDeleted the Item: "+item.getDetails());
        logData.addAuditLogItem(auditLogItem);
    }
}
