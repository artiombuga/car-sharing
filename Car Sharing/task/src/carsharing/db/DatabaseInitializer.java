package carsharing.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void populateDb(String databaseFileName) {
        try (Connection connection = new ConnectionManager(databaseFileName).getConnection();
             Statement statement = connection.createStatement()) {
//            statement.executeUpdate("DROP TABLE IF EXISTS COMPANY");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS COMPANY (" +
                                    "ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                                    "NAME VARCHAR UNIQUE NOT NULL " +
                                    ")"
            );

//            statement.executeUpdate("DROP TABLE IF EXISTS CAR");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS CAR (" +
                                    "ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                                    "NAME VARCHAR UNIQUE NOT NULL, " +
                                    "COMPANY_ID INTEGER NOT NULL, " +
                                    "foreign key(COMPANY_ID) references COMPANY(ID)" +
                                    ")"
            );

//            statement.executeUpdate("DROP TABLE IF EXISTS CUSTOMER");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                                    "ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                                    "NAME VARCHAR UNIQUE NOT NULL, " +
                                    "RENTED_CAR_ID INTEGER, " +
                                    "foreign key(RENTED_CAR_ID) references CAR(ID)" +
                                    ")"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}