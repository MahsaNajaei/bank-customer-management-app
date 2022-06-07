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
        setupLoanTypesTable(queryStatement);
        setupLoanProfilesTable(queryStatement);
        setupGrantConditionsTable(queryStatement);
    }

    private static void setupCustomersTable(Statement queryStatement) throws SQLException {
        queryStatement.execute("CREATE TABLE IF NOT EXISTS BANK_CUSTOMERS " +
                "(CUSTOMER_ID VARCHAR(15) NOT NULL, " +
                "CUSTOMER_TYPE TINYTEXT, " +
                "NAME TINYTEXT, " +
                "SURNAME TINYTEXT, " +
                "PARENT_NAME TINYTEXT, " +
                "DATE DATE, " +
                "EXCLUSIVE_ID VARCHAR(12) NOT NULL, " +
                "PRIMARY KEY (CUSTOMER_ID), " +
                "UNIQUE (EXCLUSIVE_ID))");
    }

    private static void setupLoanTypesTable(Statement queryStatement) throws SQLException {
        queryStatement.execute("CREATE TABLE IF NOT EXISTS LOAN_TYPES " +
                "(LOAN_ID INT NOT NULL AUTO_INCREMENT, " +
                "NAME TINYTEXT NOT NULL, " +
                "INTEREST_RATE DOUBLE NOT NULL, " +
                "PRIMARY KEY (LOAN_ID))");
    }


    private static void setupLoanProfilesTable(Statement queryStatement) throws SQLException {
        queryStatement.execute("CREATE TABLE IF NOT EXISTS LOAN_PROFILES " +
                "(CUSTOMER_ID VARCHAR(15) NOT NULL, " +
                "LOAN_ID INT NOT NULL, " +
                "FOREIGN KEY (CUSTOMER_ID) REFERENCES BANK_CUSTOMERS(CUSTOMER_ID), " +
                "FOREIGN KEY (LOAN_ID) REFERENCES LOAN_TYPES(LOAN_ID), " +
                "PRIMARY KEY (CUSTOMER_ID, LOAN_ID))");
    }

    private static void setupGrantConditionsTable(Statement queryStatement) throws SQLException {
        queryStatement.execute("CREATE TABLE IF NOT EXISTS GRANT_CONDITIONS " +
                "(GRANT_ID INT NOT NULL AUTO_INCREMENT, " +
                "NAME TINYTEXT NOT NULL, " +
                "MIN_CONTRACT_DURATION INT NOT NULL, " +
                "MAX_CONTRACT_DURATION INT NOT NULL, " +
                "MIN_CONTRACT_AMOUNT DECIMAL(20,3) NOT NULL, " +
                "MAX_CONTRACT_AMOUNT DECIMAL(20,3) NOT NULL, " +
                "LOAN_ID INT NOT NULL, " +
                "FOREIGN KEY (LOAN_ID) REFERENCES LOAN_TYPES(LOAN_ID), " +
                "PRIMARY KEY (GRANT_ID))");
    }


}
