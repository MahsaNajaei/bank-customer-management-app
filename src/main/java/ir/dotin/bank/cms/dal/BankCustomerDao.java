package ir.dotin.bank.cms.dal;

import ir.dotin.bank.cms.business.dto.BankCustomer;

import java.sql.SQLException;
import java.util.List;

public interface BankCustomerDao {

    void addCustomer(BankCustomer customer);

    void updateCustomer(BankCustomer bankCustomer);

    void deleteCustomer(long customerId);

    BankCustomer retrieveCustomerById(long customerId);

    boolean customerIdExists(long customerId) throws SQLException;

    List searchDBFor(String searchKey, String searchValue);
}
