package org.example.database;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.*;
import java.sql.*;

public class ConnectDatabaseMySQL {

    Dotenv dotenv = Dotenv.configure().load();

    String urlForDB = dotenv.get("DB_URL");
    String usernameForDB = dotenv.get("DB_USER");

    String passwordForDB = dotenv.get("DB_PASSWORD");


    private final String url = urlForDB;
    private final String user = usernameForDB;
    private final String password = passwordForDB;

    private final String tableName = "users";

    String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
            + "id INT AUTO_INCREMENT PRIMARY KEY,"
            + "name VARCHAR(50) NOT NULL,"
            + "age VARCHAR(100) NOT NULL"
            + ")";


    public void connectTodatabase(){

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            System.out.println("Connected to the database!");
            if (!tableExeists(connection, tableName)){
                statement.executeUpdate(createTableSQL);
                System.out.println("Table created successfully!");

            }else {
                System.out.println("Table already exists.");
            }
        }catch (SQLException e){
            System.out.println("Connection failed!");
            System.exit(1);
        }
    }

    public void saveCSVFileIntoTheDatabase(String fileName) {

        String csvFilePath = "CsvFiles/" + fileName + ".csv";

        String insertSQL = "INSERT INTO users (name, age) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
             PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {

            br.readLine();
            String line;

            while ((line = br.readLine()) != null){
                String[] data = line.split(",");
                String name = data[0].replaceAll("\"", "").trim();
                int age  = Integer.parseInt(data[1].replaceAll("\"", "").trim());

                insertStatement.setString(1, name);
                insertStatement.setInt(2, age);

                insertStatement.executeUpdate();
            }

            System.out.println("Data from CSV file inserted into MySQL database successfully.");

        } catch (SQLException | NumberFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean tableExeists(Connection connection, String tableName)throws SQLException{
        DatabaseMetaData metaData = connection.getMetaData();
        var resultSet = metaData.getTables(null, null, tableName, null);
        return resultSet.next();
    }

}
