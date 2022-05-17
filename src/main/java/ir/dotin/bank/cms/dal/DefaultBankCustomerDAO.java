package ir.dotin.bank.cms.dal;

import ir.dotin.bank.cms.business.objects.entities.CustomerEntity;
import ir.dotin.bank.cms.dal.exceptions.CustomerIdDoesNotExistsException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    public void addCustomer(CustomerEntity customer) throws SQLException {
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
    public void updateCustomer(CustomerEntity customer) throws SQLException {
        String assignmentList = extractAssignmentList(customer);
        queryStatement.execute("UPDATE bank_customers SET " + assignmentList + "WHERE customerId = " + customer.getCustomerId());
    }

    private String extractAssignmentList(CustomerEntity customer) {
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
    public CustomerEntity retrieveCustomerById(String customerId) throws SQLException, CustomerIdDoesNotExistsException {
        ResultSet resultSet = queryStatement.executeQuery("SELECT * FROM bank_customers where customerId = '" + customerId + "' ");
        if (resultSet.next()) {
            return getInitializedCustomerFromResultSet(resultSet);
        }
        throw new CustomerIdDoesNotExistsException();
    }

    private CustomerEntity getInitializedCustomerFromResultSet(ResultSet resultSet) throws SQLException {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setCustomerId(resultSet.getString("customerId"));
        customerEntity.setCustomerType(resultSet.getString("customerType"));
        customerEntity.setName(resultSet.getString("name"));
        customerEntity.setSurname(resultSet.getString("surname"));
        customerEntity.setParentName(resultSet.getString("parentName"));
        customerEntity.setDate(resultSet.getDate("date"));
        customerEntity.setExclusiveId(resultSet.getString("exclusiveId"));
        return customerEntity;
    }

    @Override
    public List<CustomerEntity> searchDBFor(String searchKey, String searchValue) throws SQLException {
        List<CustomerEntity> customers = new ArrayList<>();

        ResultSet resultSet = queryStatement.executeQuery("SELECT * from bank_customers where " + searchKey + " LIKE '" + searchValue + "%'");
        while (resultSet.next()) {
            CustomerEntity customerEntity = getInitializedCustomerFromResultSet(resultSet);
            customers.add(customerEntity);
        }

        return customers;
    }

    @Override
    public boolean exclusiveIdExists(String exclusiveId) throws SQLException {
        ResultSet resultSet = queryStatement.executeQuery("SELECT * FROM bank_customers WHERE exclusiveId = " + exclusiveId);
        return resultSet.next();
    }
}
