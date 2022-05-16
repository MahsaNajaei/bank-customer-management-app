package ir.dotin.bank.cms.dal;

import ir.dotin.bank.cms.business.dto.BankCustomer;
import ir.dotin.bank.cms.business.dto.CustomerType;
import ir.dotin.bank.cms.business.dto.LegalCustomer;
import ir.dotin.bank.cms.dal.helpers.CustomerRawDataExtractor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegalCustomerDAO implements BankCustomerDao {
    private Statement queryStatement;

    public LegalCustomerDAO() {
        connect();
    }

    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankDB", "root", "12345");
            queryStatement = connection.createStatement();
        } catch (SQLException e) {
            //Todo throw your own exception
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addCustomer(BankCustomer BankCustomer) {
        LegalCustomer legalCustomer = (LegalCustomer) BankCustomer;
        try {
            queryStatement.execute("INSERT INTO Legal_Customers values ( " +
                    legalCustomer.getCustomerId() + ", '" +
                    legalCustomer.getCompanyName() + "', '" +
                    legalCustomer.getRegistrationDate() + "', " +
                    legalCustomer.getEconomicId() + ")");
        } catch (SQLException e) {
            //Todo throw your own exception
            e.printStackTrace();
        }
    }

    @Override
    public void updateCustomer(BankCustomer bankCustomer) {
        String assignmentList = CustomerRawDataExtractor.extractAssignmentList(bankCustomer);
        try {
            queryStatement.execute("UPDATE legal_customers SET " + assignmentList + "WHERE customerId = " + bankCustomer.getCustomerId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteCustomer(long customerId) {
        try {
            queryStatement.execute("DELETE FROM legal_customers where customerId = '" + customerId + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BankCustomer retrieveCustomerById(long customerId) {
        //Todo check if customer id not exists
        LegalCustomer legalCustomer = new LegalCustomer(customerId);
        legalCustomer.setCustomerType(CustomerType.LEGAL);
        try {
            ResultSet resultSet = queryStatement.executeQuery("SELECT * FROM legal_customers where customerId= '" + customerId + "' ");
            resultSet.next();
            legalCustomer.setCompanyName(resultSet.getString("companyName"));
            legalCustomer.setEconomicId(resultSet.getString("economicId"));
            legalCustomer.setRegistrationDate(resultSet.getDate("registrationDate"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return legalCustomer;
    }

    @Override
    public boolean customerIdExists(long customerId) throws SQLException {
        ResultSet resultSet = queryStatement.executeQuery("SELECT * from legal_customers where customerId = " + customerId);
        return resultSet.next();
    }

    @Override
    public List searchDBFor(String searchKey, String searchValue) {
        List<Map<String, String>> customers = new ArrayList();
        try {
            ResultSet resultSet = queryStatement.executeQuery("SELECT * from legal_customers where "
                    + searchKey + "= \"" + searchValue + "\"");
            while (resultSet.next()) {
                Map<String, String> customerInfo = new HashMap();
                customerInfo.put("customerId", resultSet.getString("customerId"));
                customerInfo.put("companyName", resultSet.getString("companyName"));
                customerInfo.put("registrationDate", resultSet.getString("registrationDate"));
                customerInfo.put("economicId", resultSet.getString("economicId"));
                customers.add(customerInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public boolean economicIdExists(String economicId) throws SQLException {
        ResultSet resultSet = queryStatement.executeQuery("SELECT * from legal_customers where economicId = " + economicId);
        return resultSet.next();
    }
}
