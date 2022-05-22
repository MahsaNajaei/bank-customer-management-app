package ir.dotin.bank.cms.dal;

import ir.dotin.bank.cms.business.dataobjects.entities.BankCustomerEntity;
import ir.dotin.bank.cms.dal.exceptions.CustomerIdDoesNotExistsException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface BankCustomerDao {

    void addCustomer(BankCustomerEntity customer) throws SQLException;

    void updateCustomer(BankCustomerEntity customer) throws SQLException;

    void deleteCustomer(String customerId) throws SQLException;

    BankCustomerEntity retrieveCustomerById(String customerId) throws SQLException, CustomerIdDoesNotExistsException;

    List<BankCustomerEntity> searchDBFor(String searchKey, String searchValue) throws SQLException;

    List<BankCustomerEntity> searchDBFor(Map<String, String> searchKeyValues) throws SQLException;

    boolean exclusiveIdExists(String exclusiveId) throws SQLException;
}
