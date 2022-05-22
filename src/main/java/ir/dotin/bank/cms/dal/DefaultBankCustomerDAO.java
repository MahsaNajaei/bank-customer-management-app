package ir.dotin.bank.cms.dal;

import ir.dotin.bank.cms.business.dataobjects.entities.BankCustomerEntity;
import ir.dotin.bank.cms.dal.exceptions.CustomerIdDoesNotExistsException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultBankCustomerDAO implements BankCustomerDao {
    private Statement queryStatement;

    public DefaultBankCustomerDAO() {
        connect();
    }

    public void connect() {
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
    public void addCustomer(BankCustomerEntity customer) throws SQLException {
        queryStatement.execute("INSERT INTO bank_Customers values ( '" +
                customer.getCustomerId() + "', '" +
                customer.getCustomerType() + "', '" +
                customer.getName() + "', '" +
                customer.getSurname() + "', '" +
                customer.getParentName() + "', '" +
                customer.getDate() + "', '" +
                customer.getExclusiveId() + "')");
    }

    @Override
    public void updateCustomer(BankCustomerEntity customer) throws SQLException {
        String assignmentList = extractAssignmentList(customer);
        queryStatement.execute("UPDATE bank_customers SET " + assignmentList + "WHERE customerId = " + customer.getCustomerId());
    }

    private String extractAssignmentList(BankCustomerEntity customer) {
        String assignmentList = "";
        String separator = "";
        for (Method method : customer.getClass().getMethods()) {
            try {
                if (method.getName().contains("get") && !method.getName().equalsIgnoreCase("getClass")) {
                    Object methodResult = method.invoke(customer);
                    if (methodResult != null) {
                        assignmentList += (separator + method.getName().substring(3) + " = '" + methodResult + "'");
                        separator = ", ";
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return assignmentList;
    }

    @Override
    public void deleteCustomer(String customerId) throws SQLException {
        queryStatement.execute("DELETE FROM bank_customers where customerId = '" + customerId + "';");
    }

    @Override
    public BankCustomerEntity retrieveCustomerById(String customerId) throws SQLException, CustomerIdDoesNotExistsException {
        ResultSet resultSet = queryStatement.executeQuery("SELECT * FROM bank_customers where customerId = '" + customerId + "' ");
        if (resultSet.next()) {
            return getInitializedCustomerFromResultSet(resultSet);
        }
        throw new CustomerIdDoesNotExistsException();
    }

    private BankCustomerEntity getInitializedCustomerFromResultSet(ResultSet resultSet) throws SQLException {
        BankCustomerEntity bankCustomerEntity = new BankCustomerEntity();
        bankCustomerEntity.setCustomerId(resultSet.getString("customerId"));
        bankCustomerEntity.setCustomerType(resultSet.getString("customerType"));
        bankCustomerEntity.setName(resultSet.getString("name"));
        bankCustomerEntity.setSurname(resultSet.getString("surname"));
        bankCustomerEntity.setParentName(resultSet.getString("parentName"));
        bankCustomerEntity.setDate(resultSet.getDate("date"));
        bankCustomerEntity.setExclusiveId(resultSet.getString("exclusiveId"));
        return bankCustomerEntity;
    }

    @Override
    public List<BankCustomerEntity> searchDBFor(String searchKey, String searchValue) throws SQLException {
        List<BankCustomerEntity> customers = new ArrayList<>();

        ResultSet resultSet = queryStatement.executeQuery("SELECT * from bank_customers where " + searchKey + " LIKE '" + searchValue + "%'");
        while (resultSet.next()) {
            BankCustomerEntity bankCustomerEntity = getInitializedCustomerFromResultSet(resultSet);
            customers.add(bankCustomerEntity);
        }

        return customers;
    }

    @Override
    public List<BankCustomerEntity> searchDBFor(Map<String, String> searchKeyValues) throws SQLException {
        List<BankCustomerEntity> customers = new ArrayList<>();

        String query;
        if (searchKeyValues.size() != 0) {
            String conditions = extractSearchConditions(searchKeyValues);
            query = "SELECT * from bank_customers where " + conditions;
        } else {
            query = "SELECT * from bank_customers";
        }

        ResultSet resultSet = queryStatement.executeQuery(query);
        while (resultSet.next()) {
            BankCustomerEntity bankCustomerEntity = getInitializedCustomerFromResultSet(resultSet);
            customers.add(bankCustomerEntity);
        }

        return customers;
    }

    private String extractSearchConditions(Map<String, String> searchKeyValues) {
        String conditions = "";
        String operator = "";
        for (String key : searchKeyValues.keySet()) {
            String value = searchKeyValues.get(key);
            if (key.equalsIgnoreCase("date"))
                value = "%" + value;
            conditions += operator + key + " LIKE '" + value + "%'";
            operator = " AND ";
        }
        return conditions;
    }

    @Override
    public boolean exclusiveIdExists(String exclusiveId) throws SQLException {
        ResultSet resultSet = queryStatement.executeQuery("SELECT * FROM bank_customers WHERE exclusiveId = " + exclusiveId);
        return resultSet.next();
    }
}
