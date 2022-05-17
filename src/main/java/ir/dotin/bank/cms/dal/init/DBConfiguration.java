package ir.dotin.bank.cms.dal.init;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConfiguration {

    public static void setupDB() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "12345");
        Statement queryStatement = connection.createStatement();
        queryStatement.execute("CREATE DATABASE IF NOT EXISTS bankDB");
        queryStatement.execute("USE bankDB");
        setupCustomersTable(queryStatement);
    }

    private static void setupCustomersTable(Statement queryStatement) throws SQLException {
        queryStatement.execute("CREATE TABLE IF NOT EXISTS bank_customers " +
                "(customerId VARCHAR(15) NOT NULL," +
                " customerType TINYTEXT," +
                " name TINYTEXT," +
                " surname TINYTEXT," +
                " parentName TINYTEXT," +
                " date DATE," +
                " exclusiveId VARCHAR(12) NOT NULL," +
                " PRIMARY KEY (customerId)," +
                " UNIQUE (exclusiveId))");
    }

}
