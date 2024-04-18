package carsharing.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private final String driverName = "org.h2.Driver";
    private final String username = "";
    private final String password = "";
    private final String url = "jdbc:h2:C:/Internship/Car Sharing/Car Sharing/task/src/carsharing/db/";
    private final String databaseFileName;
    private Connection connection;

    public ConnectionManager(String fileName) {
        this.databaseFileName = fileName;
    }

    public Connection getConnection() {
        try {
            Class.forName(driverName);
            try {
                connection = DriverManager.getConnection(url + databaseFileName);
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Failed to create the database connection.");
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver not found.");
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Couldn't close connection");
            }
        }
    }

}
