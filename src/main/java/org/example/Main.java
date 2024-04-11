package org.example;

import com.opencsv.CSVWriter;
import org.example.database.ConnectDatabaseMySQL;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    static ConnectDatabaseMySQL CD = new ConnectDatabaseMySQL();

    public static void main(String[] args) {
        CD.connectTodatabase();
        ChooseWhatToDo();

    }

    private static void ChooseWhatToDo(){
        printMenu();
        while (true){
            String choice = sc.next();
            switch (choice){
                case "1" -> {
                    saveToCsvFile();
                    printMenu();
                }
                case "2" ->{
                    listAllfiles();
                    printMenu();
                }
                case "3" ->{
                    deleteCsvFile();
                    printMenu();
                }
                case "4" ->{
                    saveToDatabase();
                    printMenu();
                }

                case "8" -> System.exit(0);
            }
        }
    }

    private static void saveToCsvFile(){

        String fileFormat = ".csv";

        System.out.println("The file name: ");
        String fileName = sc.next() + fileFormat;

        String filePath = "CsvFiles/" + fileName;

        try(CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {

            System.out.println("How many persons do you want to add?");
            Integer addANewPerson = sc.nextInt();

            String[] header = {"Name", "Age"};
            writer.writeNext(header);

            for (int i = 0; i < addANewPerson; i++) {

                System.out.println("What name do you what to add?");
                String name = sc.next();
                System.out.println("What age do you what to add?");
                String age = sc.next();

                String[] data1 = {name, age};
                writer.writeNext(data1);

            }

            System.out.println("CSV file created successfully.");

        }catch (IOException e){
            e.printStackTrace();
        }


    }

    private static void listAllfiles(){
        String filePath = "CsvFiles/";

        List<String> csvFiles = new ArrayList<>();

        File directory = new File(filePath);

        if (directory.exists() && directory.isDirectory()){

            File[] files = directory.listFiles();

            if (files != null){
                for (File file : files){
                    if (file.isFile() && file.getName().endsWith(".csv")){
                        csvFiles.add(file.getName());
                    }
                }
            }

            System.out.println("List of CSV files:");
            for (String fileName : csvFiles){
                System.out.println(fileName);
            }
        } else {
            System.out.println("Directory does not exist or is not a directory.");
        }
    }

    private static void deleteCsvFile(){

        System.out.println("The files name: ");
        String fileName = sc.next();
        String filePath = "CsvFiles/" + fileName +".csv";

        File file = new File(filePath);

        if (file.exists()){
            if (file.delete()){
                System.out.println("CSV file deleted successfully.");
            }else {
                System.out.println("Failed to delete CSV file.");
            }
        }else {
            System.out.println("CSV file does not exist.");
        }

    }

    private static void saveToDatabase(){
        System.out.println("What file do you what to save into the database?");
        String fileName = sc.next();
        CD.saveCSVFileIntoTheDatabase(fileName);
    }

    private static void printMenu(){
        String menuText = """
                
                1. Save to CSV file.
                2. Print a list of all csv files.
                3. Delete a CSV file.
                4. Save CSV into a database.
                8. exit the program.
                
                """;
        System.out.println(menuText);
    }
}