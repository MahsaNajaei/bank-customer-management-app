package ir.dotin.bank.cms.dal.init;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConfiguration {

    public void setupDB() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "12345");
        Statement queryStatement = connection.createStatement();
        queryStatement.execute("CREATE DATABASE IF NOT EXISTS bankDB");
        queryStatement.execute("USE bankDB");
        setupLegaCustomersTable(queryStatement);
        setupNaturalCustomersTable(queryStatement);
    }

    private void setupNaturalCustomersTable(Statement queryStatement) throws SQLException {
        queryStatement.execute("CREATE TABLE IF NOT EXISTS natural_customers " +
                "(customerId VARCHAR(10) NOT NULL," +
                " name TINYTEXT," +
                " surname TINYTEXT," +
                " fathersName TINYTEXT," +
                " birthDate DATE," +
                " nationalCode VARCHAR(10) NOT NULL," +
                " PRIMARY KEY (customerId)," +
                " UNIQUE (nationalCode))");
    }

    private void setupLegaCustomersTable(Statement queryStatement) throws SQLException {
        queryStatement.execute("CREATE TABLE IF NOT EXISTS legal_customers " +
                "(customerId VARCHAR(10) NOT NULL," +
                " companyName TINYTEXT," +
                " registrationDate DATE," +
                " economicId VARCHAR(12) NOT NULL," +
                " PRIMARY KEY (customerId)," +
                " UNIQUE (economicId))");
    }
}
