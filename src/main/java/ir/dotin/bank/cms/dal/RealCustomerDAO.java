package ir.dotin.bank.cms.dal;

import ir.dotin.bank.cms.business.dto.BankCustomer;
import ir.dotin.bank.cms.business.dto.CustomerType;
import ir.dotin.bank.cms.business.dto.RealCustomer;
import ir.dotin.bank.cms.dal.helpers.CustomerRawDataExtractor;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RealCustomerDAO implements BankCustomerDao {
    private Statement queryStatement;

    public RealCustomerDAO() {
        connect();
    }

    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankDB", "root", "12345");
            queryStatement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void addCustomer(BankCustomer bankCustomer) {
        RealCustomer realCustomer = (RealCustomer) bankCustomer;
        try {
            queryStatement.execute("INSERT INTO real_Customers values ( " +
                    realCustomer.getCustomerId() + ", '" +
                    realCustomer.getName() + "', '" +
                    realCustomer.getSurname() + "', '" +
                    realCustomer.getFathersName() + "', '" +
                    realCustomer.getBirthDate() + "', " +
                    realCustomer.getNationalCode() + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void updateCustomer(BankCustomer bankCustomer) {
        String assignmentList = CustomerRawDataExtractor.extractAssignmentList(bankCustomer);
        try {
            queryStatement.execute("UPDATE real_customers SET " + assignmentList + "WHERE customerId = " + bankCustomer.getCustomerId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCustomer(long customerId) {
        try {
            queryStatement.execute("DELETE FROM real_customers where customerId = '" + customerId + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BankCustomer retrieveCustomerById(long customerId) {
        RealCustomer realCustomer = new RealCustomer(customerId);
        realCustomer.setCustomerType(CustomerType.REAL);
        try {
            ResultSet resultSet = queryStatement.executeQuery("SELECT * FROM real_customers where customerId = '" + customerId + "'");
            resultSet.next();
            realCustomer.setName(resultSet.getString("name"));
            realCustomer.setSurname(resultSet.getString("surname"));
            realCustomer.setFathersName(resultSet.getString("fathersName"));
            realCustomer.setNationalCode(resultSet.getString("nationalCode"));
            realCustomer.setBirthDate(resultSet.getDate("birthDate"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return realCustomer;
    }

    @Override
    public boolean customerIdExists(long customerId) throws SQLException {
        ResultSet resultSet = queryStatement.executeQuery("SELECT * from real_customers where customerId = " + customerId);
        return resultSet.next();
    }

    @Override
    public List searchDBFor(String searchKey, String searchValue) {
        List<Map<String, String>> customers = new ArrayList();
        try {
            ResultSet resultSet = queryStatement.executeQuery("SELECT * from real_customers where " + searchKey + " LIKE '" + searchValue + "%'");

            while (resultSet.next()) {
                Map<String, String> customerInfo = new HashMap();
                customerInfo.put("customerId", resultSet.getString("customerId"));
                customerInfo.put("name", resultSet.getString("name"));
                customerInfo.put("surname", resultSet.getString("surname"));
                customerInfo.put("fathersName", resultSet.getString("fathersName"));
                customerInfo.put("birthDate", resultSet.getString("birthDate"));
                customerInfo.put("nationalCode", resultSet.getString("nationalCode"));
                customers.add(customerInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public boolean nationalCodeExists(String nationalCode) throws SQLException {
        ResultSet resultSet = queryStatement.executeQuery("SELECT * from real_customers where nationalCode = " + nationalCode);
        return resultSet.next();
    }
}
