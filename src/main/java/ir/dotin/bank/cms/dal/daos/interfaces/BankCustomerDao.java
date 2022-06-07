package ir.dotin.bank.cms.dal.daos.interfaces;

import ir.dotin.bank.cms.business.dataobjects.entities.BankCustomerEntity;
import ir.dotin.bank.cms.business.dataobjects.entities.LoanTypeEntity;
import ir.dotin.bank.cms.dal.exceptions.CustomerNotFoundException;

import java.util.List;
import java.util.Map;

public interface BankCustomerDao {

    void addCustomer(BankCustomerEntity customer);

    void updateCustomer(BankCustomerEntity customer);

    void deleteCustomer(String customerId);

    BankCustomerEntity retrieveCustomerById(String customerId) throws CustomerNotFoundException;

    List<BankCustomerEntity> searchDBFor(String searchKey, String searchValue);

    List<BankCustomerEntity> searchDBFor(Map<String, String> searchKeyValues);

    boolean exclusiveIdExists(String exclusiveId);

    BankCustomerEntity retrieveCustomerByExclusiveId(String exclusiveId) throws CustomerNotFoundException;

    void updateCustomerLoans(String customerId, LoanTypeEntity loanType);
}
