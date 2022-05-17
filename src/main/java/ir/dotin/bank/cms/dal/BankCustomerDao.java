package ir.dotin.bank.cms.dal;

import ir.dotin.bank.cms.business.objects.entities.CustomerEntity;
import ir.dotin.bank.cms.dal.exceptions.CustomerIdDoesNotExistsException;

import java.sql.SQLException;
import java.util.List;

public interface BankCustomerDao {

    void addCustomer(CustomerEntity customer) throws SQLException;

    void updateCustomer(CustomerEntity customer) throws SQLException;

    void deleteCustomer(String customerId) throws SQLException;

    CustomerEntity retrieveCustomerById(String customerId) throws SQLException, CustomerIdDoesNotExistsException;

    List<CustomerEntity> searchDBFor(String searchKey, String searchValue) throws SQLException;

    boolean exclusiveIdExists(String exclusiveId) throws SQLException;
}
